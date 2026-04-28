package dao;

import entity.Course;
import entity.Teacher;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing {@link Teacher} entities.
 *
 * <p>This class provides methods for creating, retrieving, updating, and deleting
 * teacher data from the database. It also supports finding teachers by email
 * and handles relationships between teachers and courses.</p>
 *
 */
public class TeacherDao {

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the DAO class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(TeacherDao.class.getName());

    /**
     * Persists a new {@link Teacher} entity to the database.
     *
     * @param teacher the Teacher entity to be persisted
     */
    public void persist(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.persist(teacher);
        em.getTransaction().commit();
    }

    /**
     * Finds a {@link Teacher} entity by its ID.
     *
     * @param id the unique ID of the Teacher entity
     * @return the Teacher entity if found, otherwise null
     */
    public Teacher find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        return em.find(Teacher.class, id);
    }

    /**
     * Finds a {@link Teacher} entity by its email address.
     *
     * @param email the email address of the teacher
     * @return the Teacher entity if found, otherwise null
     */
    public Teacher findByEmail(String email){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            return em.createQuery("select t from Teacher t WHERE t.email = :tEmail",
                            Teacher.class)
                    .setParameter("tEmail", email)
                    .getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Teacher instance not found.", e);
            return null;
        }
    }

    /**
     * Updates an existing {@link Teacher} entity in the database.
     *
     * @param teacher the Teacher entity to update
     */
    public void update(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(teacher);
        em.getTransaction().commit();
    }

    /**
     * Deletes a {@link Teacher} entity from the database.
     *
     * <p>Before deletion, all {@link Course} entities associated with the teacher
     * will have their teacher reference set to {@code null} to maintain referential integrity.</p>
     *
     * @param teacherId the unique ID of the Teacher entity to delete
     */
    public void delete(int teacherId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        Teacher teacher = em.find(Teacher.class, teacherId);

        // Manually set all teacher's courses' teacher_id as NULL.
        List<Course> courses = em.createQuery(
                "SELECT c FROM Course c WHERE c.teacher = :teacher",
                Course.class
        ).setParameter("teacher", teacher).getResultList();

        for (Course c : courses) {
            c.setTeacher(null);
        }

        em.remove(teacher);
        em.getTransaction().commit();
        em.close();
    }
}
