package dao;

import entity.Student;
import jakarta.persistence.EntityManager;

import java.util.*;

public class StudentDao {
    public void persist(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }

    public Student find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(Student.class, id);
    }

    public void update(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(student);
        em.getTransaction().commit();
    }

    public void delete(Student student) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(student);
        em.getTransaction().commit();
    }
}
