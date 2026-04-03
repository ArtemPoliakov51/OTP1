package dao;

import entity.AttendanceCheck;
import entity.Attends;
import entity.Teacher;
import entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;


/**
 * Data Access Object class for the Course entity
 *
 * @version 1.0
 */
public class CourseDao {
    /**
     * Add an instance of the Course entity to the database
     *
     * @param teacherId The id of Teacher entity instance
     */
    public int persist(String nameEN, String nameFI, String nameJA, String nameEL, String identifier, int teacherId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Teacher teacher = em.find(Teacher.class, teacherId);
        Course course = new Course(nameEN, nameFI, nameJA, nameEL, identifier, teacher);

        em.persist(course);

        em.getTransaction().commit();
        em.close();
        return course.getId();
    }

    /**
     * Find an instance of the Course entity from the database
     *
     * @param id The unique id of Course entity instance
     * @return the Course entity instance if found, null if instance not found
     */
    public Course find(int id) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
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
     * @param teacherId The id of the Teacher entity instance
     * @return the list of Course entity instances if found, null if instances not found
     */
    public List<Course> findByTeacher(int teacherId) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
            Teacher teacher = em.find(Teacher.class, teacherId);
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
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(course);
        em.getTransaction().commit();
    }

    /**
     * Delete the Course entity instance from the database
     *
     * @param courseId The id of Course entity instance to be deleted
     */
    public void delete(int courseId) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();

        Course course = em.find(Course.class, courseId);

        for (Attends attends : course.getAttends()) {
            em.remove(attends);
        }

        em.remove(course);
        em.getTransaction().commit();
        em.close();
    }
}
