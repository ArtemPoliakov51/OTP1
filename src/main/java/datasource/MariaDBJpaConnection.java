package datasource;

import jakarta.persistence.*;

public class MariaDBJpaConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {

        if (em==null) {
            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("AttendanceCheckerMariaDbUnit");
            }
            em = emf.createEntityManager();
        }
        return em;
    }

    public static void reset() {
        em = null;
    }
}
