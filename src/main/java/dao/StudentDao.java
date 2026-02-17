package dao;

import entity.Student;
import jakarta.persistence.EntityManager;

import java.util.*;

/**
 * Data Access Object class for the Student entity
 *
 * @version 1.0
 */
public class StudentDao {
    /**
     * Add an instance of the Student entity to the database
     *
     * @param student The Student entity instance to be added
     */
    public void persist(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }

    /**
     * Find an instance of the Student entity from the database
     *
     * @param id The unique id of Student entity instance
     * @return the Student entity instance if found
     */
    public Student find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(Student.class, id);
    }

    /**
     * Update the Student entity instance in the database
     *
     * @param student The Student entity instance to be updated
     */
    public void update(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(student);
        em.getTransaction().commit();
    }

    /**
     * Delete the Student entity instance from the database
     *
     * @param student The Student entity instance to be deleted
     */
    public void delete(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(student);
        em.getTransaction().commit();
    }
}
