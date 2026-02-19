package dao;

import entity.AttendanceCheck;
import entity.Checks;
import entity.Course;
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
     * Find all instances of the Student entity from the database
     *
     * @return the list of Student entity instances if found, null if not found
     */
    public List<Student> findAll() {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Student> students = em.createQuery("select s from Student s",
                            Student.class).getResultList();
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
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

        for (Checks checks : student.getChecks()) {
            em.remove(checks);
        }

        em.remove(student);
        em.getTransaction().commit();
    }
}
