package dao;

import entity.AttendanceCheck;
import entity.Checks;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.*;

/**
 * Data Access Object class for the Checks entity
 * @version 1.0
 */
public class ChecksDao {
    /**
     * Add an instance of the Checks entity to the database
     * @param checks The Checks entity instance to be added
     */
    public void persist(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(checks);
        em.getTransaction().commit();
    }

    /**
     * Find an instance of the Checks entity from the database
     * @param id The unique id of Checks entity instance
     * @return the Checks entity instance
     */
    public Checks find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(Checks.class, id);
    }

    /**
     * Find all Checks instances from the database that are associated with an AttendanceCheck instance
     * @param attendanceCheck The AttendanceCheck entity instance
     * @return the list of Checks entity instances if found, null if instances not found
     */
    public List<Checks> findByAttendanceCheck(AttendanceCheck attendanceCheck){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Checks> checks = em.createQuery("select ch from Checks ch WHERE ch.attendanceCheck = :chAttCheck",
                            Checks.class)
                    .setParameter("chAttCheck", attendanceCheck)
                    .getResultList();
            return checks;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Update the Checks entity instance in the database
     * @param checks The Checks entity instance to be updated
     */
    public void update(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(checks);
        em.getTransaction().commit();
    }

    /**
     * Delete the Checks entity instance from the database
     * @param checks The Checks entity instance to be deleted
     */
    public void delete(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(checks);
        em.getTransaction().commit();
    }
}
