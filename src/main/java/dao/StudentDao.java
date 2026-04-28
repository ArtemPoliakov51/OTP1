package dao;

import entity.*;
import jakarta.persistence.EntityManager;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing {@link Student} entities.
 *
 * <p>This class provides methods for creating, retrieving, updating, and deleting
 * student data from the database.</p>
 *
 */
public class StudentDao {

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the DAO class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(StudentDao.class.getName());

    /**
     * Persists a new {@link Student} entity to the database.
     *
     * @param student the Student entity to be persisted
     */
    public void persist(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }

    /**
     * Finds a {@link Student} entity by its ID.
     *
     * @param id the unique ID of the Student entity
     * @return the Student entity if found, otherwise null
     */
    public Student find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        return em.find(Student.class, id);
    }

    /**
     * Retrieves all {@link Student} entities from the database.
     *
     * @return a list of Student entities if found, or an empty list if no data is found
     */
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            students = em.createQuery("select s from Student s",
                            Student.class).getResultList();
            return students;
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "No Student instances found.");
            return students;
        }
    }

    /**
     * Updates an existing {@link Student} entity in the database.
     *
     * @param student the Student entity to update
     */
    public void update(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(student);
        em.getTransaction().commit();
    }

    /**
     * Deletes a {@link Student} entity from the database.
     *
     * @param studentId the unique ID of the Student entity to delete
     */
    public void delete(int studentId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        Student student = em.find(Student.class, studentId);
        em.remove(student);
        em.getTransaction().commit();
        em.close();
    }
}
