package dao;

import entity.AttendanceCheck;
import jakarta.persistence.EntityManager;

import java.util.*;

public class AttendanceCheckDao {
    public void persist(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(attendanceCheck);
        em.getTransaction().commit();
    }

    public AttendanceCheck find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(AttendanceCheck.class, id);
    }

    public void update(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(attendanceCheck);
        em.getTransaction().commit();
    }

    public void delete(AttendanceCheck attendanceCheck) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(attendanceCheck);
        em.getTransaction().commit();
    }
}
