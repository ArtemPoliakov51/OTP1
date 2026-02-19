package dao;

import entity.Attends;
import entity.AttendsId;
import entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data Access Object class for the Attends entity
 * @version 1.0
 */
public class AttendsDao {
    /**
     * Add an instance of the Attends entity to the database
     * @param attends The Attends entity instance to be added
     */
    public void persist(Attends attends) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(attends);
        em.getTransaction().commit();
    }

    /**
     * Find an instance of the Attends entity from the database
     * @param courseId The unique id of Course entity instance
     * @param studentId The unique id of Student entity instance
     * @return the Attends entity instance if found, null if instance not found
     */
    public Attends find(int courseId, int studentId) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            AttendsId id = new AttendsId(courseId, studentId);
            return em.find(Attends.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Find all Attends instances from the database that are associated with a Course instance
     * @param course The Course entity instance
     * @return the list of Attends entity instances if found, null if instances not found
     */
    public List<Attends> findByCourse(Course course){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Attends> attends = em.createQuery("select a from Attends a WHERE a.course = :aCourse",
                            Attends.class)
                    .setParameter("aCourse", course)
                    .getResultList();
            return attends;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Update the Attends entity instance in the database
     * @param attends The Attends entity instance to be updated
     */
    public void update(Attends attends) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(attends);
        em.getTransaction().commit();
    }

    /**
     * Delete the Attends entity instance from the database
     * @param courseId The unique id of Course entity instance
     * @param studentId The unique id of Student entity instance
     */
    public void delete(int courseId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();

        AttendsId id = new AttendsId(courseId, studentId);
        Attends managed = em.find(Attends.class, id);

        if (managed != null) {
            em.remove(managed);
        }

        em.getTransaction().commit();
    }
}
