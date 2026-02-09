package dao;

import entity.Attends;
import jakarta.persistence.EntityManager;

public class AttendsDao {
    public void persist(Attends attends) {
        EntityManager em = datasource.MariaDBJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(attends);
        em.getTransaction().commit();
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
