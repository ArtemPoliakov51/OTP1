package dao;

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

    public String getEmail(int id) {
        String email = "";
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Teacher> teachers = em.createQuery("select t from Teacher t").getResultList();
            for (Teacher teacher : teachers) {
                if (teacher.getId() == id) {
                    email = teacher.getEmail();
                }
            }
        } catch (Exception e) {
            System.out.println("Data not found.");
        }
        return email;
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
        em.remove(teacher);
        em.getTransaction().commit();
    }
}
