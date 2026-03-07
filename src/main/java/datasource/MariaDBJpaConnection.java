package datasource;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

public class MariaDBJpaConnection {

    private static EntityManagerFactory emf = null;

    public synchronized static EntityManager getEntityManager() {
        Map<String, String> props = new HashMap<>();

        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:mariadb://localhost:3306/attendance_database");
        String dbUser = System.getenv().getOrDefault("DB_USER", "attendance_user");
        String dbPassword = System.getenv().getOrDefault("DB_PASSWORD", "attendance_password");

        props.put("jakarta.persistence.jdbc.url", dbUrl);
        props.put("jakarta.persistence.jdbc.user", dbUser);
        props.put("jakarta.persistence.jdbc.password", dbPassword);

        if (emf==null) {
            emf = Persistence.createEntityManagerFactory("AttendanceCheckerMariaDbUnit", props);
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
