package dao;

import entity.Attends;
import entity.AttendsId;
import entity.Course;
import entity.Student;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data Access Object class for the Attends entity
 * @version 1.0
 */
public class AttendsDao {
    /**
     * Add an instance of the Attends entity to the database
     */
    public void persist(int courseId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Course course = em.find(Course.class, courseId);
        Student student = em.find(Student.class, studentId);

        Attends attends = new Attends(course, student);

        em.persist(attends);

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Find an instance of the Attends entity from the database
     * @param courseId The unique id of Course entity instance
     * @param studentId The unique id of Student entity instance
     * @return the Attends entity instance if found, null if instance not found
     */
    public Attends find(int courseId, int studentId) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            return em.find(Attends.class, new AttendsId(courseId, studentId));
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
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
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
     * Find all Attends instances from the database that are associated with a Student instance
     * @param student The Student entity instance
     * @return the list of Attends entity instances if found, null if instances not found
     */
    public List<Attends> findByStudent(Student student){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            List<Attends> attends = em.createQuery("select a from Attends a WHERE a.student = :aStudent",
                            Attends.class)
                    .setParameter("aStudent", student)
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
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(attends);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Delete the Attends entity instance from the database
     * @param courseId The unique id of Course entity instance
     * @param studentId The unique id of Student entity instance
     */
    public void delete(int courseId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Attends attends = em.find(Attends.class, new AttendsId(courseId, studentId));

        if (attends != null) {
            em.remove(attends);
        }

        em.getTransaction().commit();
        em.close();
    }
}
