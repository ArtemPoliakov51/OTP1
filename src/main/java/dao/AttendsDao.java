package dao;

import entity.Attends;
import entity.Course;
import entity.Teacher;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AttendsDao {
    public void persist(Attends attends) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(attends);
        em.getTransaction().commit();
    }

    public Attends find(int id) {
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            return em.find(Attends.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    public List<Attends> findByCourse(Course course){
        try {
            EntityManager em = datasource.MariaDBJpaConnection.getInstance();
            List<Attends> attends = em.createQuery("select a from Attends a WHERE a.course = :aCourse",
                            Attends.class)
                    .setParameter("aCourse", course)
                    .getResultList();
            return attends;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Data not found.");
            return null;
        }
    }

    public void update(Attends attends) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(attends);
        em.getTransaction().commit();
    }

    public void delete(Attends attends) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(attends);
        em.getTransaction().commit();
    }
}
