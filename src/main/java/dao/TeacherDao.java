package dao;

import entity.Course;
import entity.Teacher;
import jakarta.persistence.EntityManager;

import java.util.List;

import java.util.*;

public class TeacherDao {
    public void persist(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(teacher);
        em.getTransaction().commit();
    }

    public Teacher find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(Teacher.class, id);
    }

    public void update(Teacher teacher) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(teacher);
        em.getTransaction().commit();
    }

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
