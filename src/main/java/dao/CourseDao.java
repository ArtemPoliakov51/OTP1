package dao;

import entity.Attends;
import entity.Teacher;
import entity.Course;
import jakarta.persistence.EntityManager;

import java.util.List;


/**
 * Data Access Object (DAO) for managing {@link Course} entities.
 *
 * <p>This class provides methods for creating, retrieving, updating, and deleting
 * course data from the database. It also supports fetching courses associated
 * with a specific {@link Teacher}.</p>
 *
 */
public class CourseDao {

    /**
     * Persists a new {@link Course} entity to the database.
     *
     * <p>The course is created with multilingual names and linked to a specific teacher.</p>
     *
     * @param nameEN the course name in English
     * @param nameFI the course name in Finnish
     * @param nameJA the course name in Japanese
     * @param nameEL the course name in Greek
     * @param identifier the unique course identifier (e.g. course code)
     * @param teacherId the unique ID of the Teacher entity
     * @return the ID of the created Course entity
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
     * Finds a {@link Course} entity by its ID.
     *
     * @param id the unique ID of the Course entity
     * @return the Course entity if found, otherwise null
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
     * Retrieves all {@link Course} entities associated with a specific teacher.
     *
     * @param teacherId the unique ID of the Teacher entity
     * @return a list of Course entities if found, or null if no data is found
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
     * Updates an existing {@link Course} entity in the database.
     *
     * @param course the Course entity to update
     */
    public void update(Course course) {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.merge(course);
        em.getTransaction().commit();
    }

    /**
     * Deletes a {@link Course} entity and its related {@link Attends} entities.
     *
     * <p>This ensures that all student-course relationships are removed
     * before deleting the course.</p>
     *
     * @param courseId the unique ID of the Course entity to delete
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
