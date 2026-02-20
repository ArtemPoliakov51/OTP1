package dao;

import entity.AttendanceCheck;
import entity.Checks;
import entity.Course;
import jakarta.persistence.EntityManager;
import org.hibernate.annotations.Check;

import java.util.*;

/**
 * Data Access Object class for the AttendanceCheck entity
 * @version 1.0
 */
public class AttendanceCheckDao {
    /**
     * Add an instance of the AttendanceCheck entity to the database
     * @param attendanceCheck The AttendanceCheck entity instance to be added
     */
    public void persist(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(attendanceCheck);
        em.getTransaction().commit();
    }

    /**
     * Find an instance of the AttendanceCheck entity from the database
     * @param id The unique id of AttendanceCheck entity instance
     * @return the AttendanceCheck entity instance
     */
    public AttendanceCheck find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(AttendanceCheck.class, id);
    }

    /**
     * Find all AttendanceCheck instances from the database that are associated with a Course instance
     * @param course The Course entity instance
     * @return the list of AttendanceCheck entity instances if found, null if instances not found
     */
    public List<AttendanceCheck> findByCourse(Course course){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
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
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(attendanceCheck);
        em.getTransaction().commit();
    }

    /**
     * Delete the AttendanceCheck entity instance from the database
     * @param attendanceCheck The AttendanceCheck entity instance to be deleted
     */
    public void delete(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();

        for (Checks checks : attendanceCheck.getChecks()) {
            em.remove(checks);
        }

        em.remove(attendanceCheck);
        em.getTransaction().commit();
        em.clear();
    }
}
