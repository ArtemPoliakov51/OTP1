package dao;

import entity.AttendanceCheck;
import entity.Checks;
import entity.Course;
import jakarta.persistence.EntityManager;

import java.util.*;

/**
 * Data Access Object class for the AttendanceCheck entity
 * @version 1.0
 */
public class AttendanceCheckDao {
    /**
     * Add an instance of the AttendanceCheck entity to the database
     * @param courseId The id of the Course entity instance
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
     * Find an instance of the AttendanceCheck entity from the database
     * @param id The unique id of AttendanceCheck entity instance
     * @return the AttendanceCheck entity instance
     */
    public AttendanceCheck find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        return em.find(AttendanceCheck.class, id);
    }

    /**
     * Find all AttendanceCheck instances from the database that are associated with a Course instance
     * @param courseId The id of Course entity instance
     * @return the list of AttendanceCheck entity instances if found, null if instances not found
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
     * Update the AttendanceCheck entity instance in the database
     * @param attendanceCheck The AttendanceCheck entity instance to be updated
     */
    public void update(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(attendanceCheck);
        em.getTransaction().commit();
    }

    /**
     * Delete the AttendanceCheck entity instance from the database
     * @param attendanceCheckId The id of AttendanceCheck entity instance to be deleted
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
