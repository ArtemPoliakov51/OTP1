package datasource;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing JPA {@link EntityManager} instances
 * connected to a MariaDB database.
 *
 * <p>This class lazily initializes a singleton {@link EntityManagerFactory}
 * and provides methods for obtaining {@link EntityManager} instances
 * for both production and testing environments.</p>
 *
 * <p>Database configuration is read from environment variables:
 * <ul>
 *   <li>DB_URL</li>
 *   <li>DB_USER</li>
 *   <li>DB_PASSWORD</li>
 * </ul>
 * Default values are used if environment variables are not set.</p>
 */
public class MariaDBJpaConnection {

    /**
     * The {@link EntityManagerFactory} instance used for creating {@link EntityManager} instances
     */
    private static EntityManagerFactory emf = null;

    /**
     * Returns an {@link EntityManager} connected to the main database.
     *
     * <p>If the {@link EntityManagerFactory} has not been initialized,
     * it will be created using environment configuration.</p>
     *
     * @return a new {@link EntityManager} instance
     */
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

    /**
     * Returns an {@link EntityManager} configured for testing.
     *
     * <p>This method uses a separate persistence unit intended for test environments.</p>
     *
     * @return a new test {@link EntityManager} instance
     */
    public synchronized static EntityManager getTestEntityManager() {

            if (emf==null) {
                emf = Persistence.createEntityManagerFactory("AttendanceCheckerTestMariaDbUnit");
            }
          return emf.createEntityManager();
    }

    /**
     * Resets the {@link EntityManagerFactory}.
     *
     * <p>This method is primarily intended for testing purposes,
     * allowing the factory to be reinitialized.</p>
     */
    public synchronized static void reset() {
        emf = null;
    }
}
