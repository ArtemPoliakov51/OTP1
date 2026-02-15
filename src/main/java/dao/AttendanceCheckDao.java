package dao;

import entity.AttendanceCheck;
import entity.Checks;
import entity.Course;
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

    public List<AttendanceCheck> findByCourse(Course course){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<AttendanceCheck> attChecks = em.createQuery("select atc from AttendanceCheck atc WHERE atc.course = :atcCourse",
                            AttendanceCheck.class)
                    .setParameter("atcCourse", course)
                    .getResultList();
            return attChecks;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
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
