package dao;

import entity.Course;
import entity.Teacher;
import jakarta.persistence.EntityManager;

import java.util.List;

import java.util.*;

/**
 * Data Access Object class for the Teacher entity
 *
 * @version 1.0
 */
public class TeacherDao {
    /**
     * Add an instance of the Teacher entity to the database
     *
     * @param teacher The Teacher entity instance to be added
     */
    public void persist(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(teacher);
        em.getTransaction().commit();
    }

    /**
     * Find an instance of the Teacher entity from the database
     *
     * @param id The unique id of Teacher entity instance
     * @return the Teacher entity instance if found
     */
    public Teacher find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(Teacher.class, id);
    }

    /**
     * Find a Teacher instances from the database that has a specific email attribute value
     *
     * @param email The email in string format
     * @return the Teacher entity instance if found, null if instance not found
     */
    public Teacher findByEmail(String email){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            Teacher teacher = em.createQuery("select t from Teacher t WHERE t.email = :tEmail",
                            Teacher.class)
                    .setParameter("tEmail", email)
                    .getSingleResult();
            return teacher;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Update the Teacher entity instance in the database
     *
     * @param teacher The Teacher entity instance to be updated
     */
    public void update(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(teacher);
        em.getTransaction().commit();
    }

    /**
     * Delete the Teacher entity instance from the database and set associated Course entity instances' teacher attribute value to null
     *
     * @param teacher The Teacher entity instance to be deleted
     */
    public void delete(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();

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
    }
}
