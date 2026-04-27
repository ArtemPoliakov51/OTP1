package dao;

import entity.*;
import jakarta.persistence.EntityManager;

import java.util.*;

/**
 * Data Access Object (DAO) for managing {@link Student} entities.
 *
 * <p>This class provides methods for creating, retrieving, updating, and deleting
 * student data from the database.</p>
 *
 */
public class StudentDao {

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
     * @return a list of Student entities if found, or null if no data is found
     */
    public List<Student> findAll() {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            List<Student> students = em.createQuery("select s from Student s",
                            Student.class).getResultList();
            return students;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
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
