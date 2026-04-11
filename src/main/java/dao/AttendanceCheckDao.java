package dao;

import entity.AttendanceCheck;
import entity.Checks;
import entity.Course;
import jakarta.persistence.EntityManager;

import java.util.*;

/**
 * Data Access Object (DAO) for managing {@link AttendanceCheck} entities.
 *
 * <p>This class provides methods for creating, retrieving, updating, and deleting
 * attendance check records from the database. It also supports fetching attendance
 * checks associated with a specific course.</p>
 */
public class AttendanceCheckDao {

    /**
     * Persists a new {@link AttendanceCheck} entity for a given course.
     *
     * <p>A new attendance check is created and linked to the specified course.</p>
     *
     * @param courseId the ID of the course to which the attendance check belongs
     * @return the ID of the newly created AttendanceCheck entity
     */
    public int persist(int courseId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Course course = em.find(Course.class, courseId);
        AttendanceCheck attendanceCheck = new AttendanceCheck(course);

        em.persist(attendanceCheck);

        em.getTransaction().commit();
        em.close();
        return attendanceCheck.getId();
    }

    /**
     * Finds an {@link AttendanceCheck} by its ID.
     *
     * @param id the unique identifier of the attendance check
     * @return the found AttendanceCheck entity, or null if not found
     */
    public AttendanceCheck find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        return em.find(AttendanceCheck.class, id);
    }

    /**
     * Retrieves all {@link AttendanceCheck} entities associated with a given course.
     *
     * @param courseId the ID of the course
     * @return a list of AttendanceCheck entities or null if no attendance checks were found
     */
    public List<AttendanceCheck> findByCourse(int courseId){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            Course course = em.find(Course.class, courseId);
            List<AttendanceCheck> attChecks = em.createQuery("select atc from AttendanceCheck atc WHERE atc.course = :atcCourse",
                            AttendanceCheck.class)
                    .setParameter("atcCourse", course)
                    .getResultList();
            return attChecks;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }


    /**
     * Updates an existing {@link AttendanceCheck} entity in the database.
     *
     * @param attendanceCheck the AttendanceCheck entity to update
     */
    public void update(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(attendanceCheck);
        em.getTransaction().commit();
    }

    /**
     * Deletes an {@link AttendanceCheck} entity and all its related {@link Checks}.
     *
     * <p>This ensures referential integrity by removing associated child entities
     * before deleting the parent attendance check.</p>
     *
     * @param attendanceCheckId the ID of the attendance check to delete
     */
    public void delete(int attendanceCheckId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        AttendanceCheck attendanceCheck = em.find(AttendanceCheck.class, attendanceCheckId);

        for (Checks checks : attendanceCheck.getChecks()) {
            em.remove(checks);
        }

        em.remove(attendanceCheck);
        em.getTransaction().commit();
        em.close();
    }
}
