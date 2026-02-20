package dao;

import entity.*;
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
     * @param attCheckId The unique id of the AttendanceCheck entity instance
     * @param studentId The unique id of the Student entity instance
     * @return the Checks entity instance
     */
    public Checks find(int attCheckId, int studentId) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            ChecksId id = new ChecksId(attCheckId, studentId);
            return em.find(Checks.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
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
     * Find all Checks instances from the database that are associated with a Student instance
     * @param student The Student entity instance
     * @return the list of Checks entity instances if found, null if instances not found
     */
    public List<Checks> findByStudent(Student student){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Checks> checks = em.createQuery("select ch from Checks ch WHERE ch.student = :chStudent",
                            Checks.class)
                    .setParameter("chStudent", student)
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
     * @param attCheckId The unique id of the AttendanceCheck entity instance
     * @param studentId The unique id of the Student entity instance
     */
    public void delete(int attCheckId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();

        int deletedCount = em.createQuery(
                        "DELETE FROM Checks c WHERE c.attendanceCheck.id = :attCheckId AND c.student.id = :studentId")
                .setParameter("attCheckId", attCheckId)
                .setParameter("studentId", studentId)
                .executeUpdate();

        System.out.println("Deleted rows: " + deletedCount);

        em.getTransaction().commit();
        em.clear();
    }
}
