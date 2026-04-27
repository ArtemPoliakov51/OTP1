package dao;

import entity.*;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Data Access Object (DAO) for managing {@link Checks} entities.
 *
 * <p>This class provides methods for creating, retrieving, updating, and deleting
 * {@link Checks} entities, which represent attendance records for students
 * in specific {@link AttendanceCheck} events.</p>
 *
 * <p>The {@link Checks} entity uses a composite primary key ({@link ChecksId})
 * consisting of an attendance check ID and a student ID.</p>
 *
 */
public class ChecksDao {

    /**
     * Persists a new {@link Checks} entity linking a student to an attendance check.
     *
     * @param attendanceCheckId the unique ID of the AttendanceCheck entity
     * @param studentId the unique ID of the Student entity
     * @return the composite ID of the created Checks entity
     */
    public ChecksId persist(int attendanceCheckId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Student student = em.find(Student.class, studentId);
        AttendanceCheck attendanceCheck = em.find(AttendanceCheck.class, attendanceCheckId);

        Checks checks = new Checks(student, attendanceCheck);

        em.persist(checks);

        em.getTransaction().commit();
        em.close();
        return checks.getId();
    }

    /**
     * Finds a {@link Checks} entity by its composite key.
     *
     * @param attCheckId the unique ID of the AttendanceCheck entity
     * @param studentId the unique ID of the Student entity
     * @return the Checks entity if found, otherwise null
     */
    public Checks find(int attCheckId, int studentId) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            ChecksId id = new ChecksId(attCheckId, studentId);
            return em.find(Checks.class, id);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Retrieves all {@link Checks} entities associated with a specific attendance check.
     *
     * @param attendanceCheckId the unique ID of the AttendanceCheck entity
     * @return a list of Checks entities if found, or null if no data is found
     */
    public List<Checks> findByAttendanceCheck(int attendanceCheckId){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            AttendanceCheck attendanceCheck = em.find(AttendanceCheck.class, attendanceCheckId);
            List<Checks> checks = em.createQuery("select ch from Checks ch WHERE ch.attendanceCheck = :chAttCheck",
                            Checks.class)
                    .setParameter("chAttCheck", attendanceCheck)
                    .getResultList();
            return checks;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Retrieves all {@link Checks} entities associated with a specific student.
     *
     * @param studentId the unique ID of the Student entity
     * @return a list of Checks entities if found, or null if no data is found
     */
    public List<Checks> findByStudent(int studentId){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            Student student = em.find(Student.class, studentId);
            List<Checks> checks = em.createQuery("select ch from Checks ch WHERE ch.student = :chStudent",
                            Checks.class)
                    .setParameter("chStudent", student)
                    .getResultList();
            return checks;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Updates an existing {@link Checks} entity in the database.
     *
     * @param checks the Checks entity to update
     */
    public void update(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(checks);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Deletes a {@link Checks} entity using its composite key.
     *
     * @param attCheckId the unique ID of the AttendanceCheck entity
     * @param studentId the unique ID of the Student entity
     */
    public void delete(int attCheckId, int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Checks checks = em.find(Checks.class, new ChecksId(attCheckId, studentId));

        if (checks != null) {
            em.remove(checks);
        }

        em.getTransaction().commit();
        em.close();
    }
}
