package datasource;

import jakarta.persistence.*;

public class MariaDBJpaConnection {

    private static EntityManagerFactory emf = null;

    public synchronized static EntityManager getEntityManager() {
        if (emf==null) {
            emf = Persistence.createEntityManagerFactory("AttendanceCheckerMariaDbUnit");
        }
        return emf.createEntityManager();
    }

    public synchronized static EntityManager getTestEntityManager() {

            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("AttendanceCheckerTestMariaDbUnit");
            }
          return emf.createEntityManager();
    }

    public synchronized static void reset() {
        emf = null;
    }
}
