package datasource;

import jakarta.persistence.*;

public class MariaDBJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public synchronized static EntityManager getInstance() {

        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("AttendanceCheckerMariaDbUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }

    public synchronized static EntityManager getTestInstance() {

        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("AttendanceCheckerTestMariaDbUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }

    public static void reset() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        em = null;
        emf = null;
    }
}
