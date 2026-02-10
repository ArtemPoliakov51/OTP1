package dao;

import entity.AttendanceCheck;
import entity.Checks;
import jakarta.persistence.EntityManager;

import java.util.List;

import entity.Course;

import java.util.*;

public class ChecksDao {
    public void persist(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(checks);
        em.getTransaction().commit();
    }

    public Checks find(int id) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        return em.find(Checks.class, id);
    }

    public List<Checks> findByAttendanceCheck(AttendanceCheck attendanceCheck){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Checks> checks = em.createQuery("select ch from Checks ch WHERE ch.attendanceCheck = :chAttCheck",
                            Checks.class)
                    .setParameter("chAttCheck", attendanceCheck)
                    .getResultList();
            return checks;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    public void update(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(checks);
        em.getTransaction().commit();
    }

    public void delete(Checks checks) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(checks);
        em.getTransaction().commit();
    }
}
