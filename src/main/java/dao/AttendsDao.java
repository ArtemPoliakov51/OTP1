package dao;

import entity.Attends;
import entity.AttendsId;
import entity.Course;
import entity.Student;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data Access Object (DAO) for managing {@link Attends} entities.
 *
 * <p>This class provides methods for creating, retrieving, and deleting
 * relationships between {@link Course} and {@link Student} entities.</p>
 *
 * <p>The {@link Attends} entity represents a many-to-many relationship
 * between courses and students using a composite primary key ({@link AttendsId}).</p>
 *
 */
public class AttendsDao {

    /**
     * Persists a new {@link Attends} entity linking a student to a course.
     *
     * @param courseId the unique ID of the course
     * @param studentId the unique ID of the student
     * @return the composite ID of the created Attends entity
     */
    public AttendsId persist(int courseId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Course course = em.find(Course.class, courseId);
        Student student = em.find(Student.class, studentId);

        Attends attends = new Attends(course, student);

        em.persist(attends);

        em.getTransaction().commit();
        em.close();
        return attends.getId();
    }

    /**
     * Finds an {@link Attends} entity by its composite key.
     *
     * @param courseId the unique ID of the course
     * @param studentId the unique ID of the student
     * @return the Attends entity if found, otherwise null
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
     * Retrieves all {@link Attends} entities associated with a specific course.
     *
     * @param courseId the unique ID of the course
     * @return a list of Attends entities if found, or null if no data is found
     */
    public List<Attends> findByCourse(int courseId){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            Course course = em.find(Course.class, courseId);
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
     * Retrieves all {@link Attends} entities associated with a specific student.
     *
     * @param student the Student entity
     * @return a list of Attends entities if found, or null if no data is found
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
     * Deletes an {@link Attends} entity using its composite key.
     *
     * @param courseId the unique ID of the course
     * @param studentId the unique ID of the student
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
