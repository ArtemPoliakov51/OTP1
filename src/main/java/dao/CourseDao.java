package dao;

import entity.Teacher;
import entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.*;


/**
 * Data Access Object class for the Course entity
 *
 * @version 1.0
 */
public class CourseDao {
    /**
     * Add an instance of the Course entity to the database
     *
     * @param course The Course entity instance to be added
     */
    public void persist(Course course) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();
    }

    /**
     * Find an instance of the Course entity from the database
     *
     * @param id The unique id of Course entity instance
     * @return the Course entity instance if found, null if instance not found
     */
    public Course find(int id) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            return em.find(Course.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }


    /**
     * Find all Course instances from the database that are associated with a Teacher instance
     *
     * @param teacher The Teacher entity instance
     * @return the list of Course entity instances if found, null if instances not found
     */
    public List<Course> findByTeacher(Teacher teacher) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Course> courses = em.createQuery("select c from Course c WHERE c.teacher = :cTeach",
                            Course.class)
                    .setParameter("cTeach", teacher)
                    .getResultList();
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    /**
     * Update the Course entity instance in the database
     *
     * @param course The Course entity instance to be updated
     */
    public void update(Course course) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(course);
        em.getTransaction().commit();
    }

    /**
     * Delete the Course entity instance from the database
     *
     * @param course The Course entity instance to be deleted
     */
    public void delete(Course course) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(course);
        em.getTransaction().commit();
    }
}
