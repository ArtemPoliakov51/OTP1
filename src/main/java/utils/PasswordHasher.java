package utils;

import org.mindrot.jbcrypt.BCrypt;


/**
 * Utility class for hashing and verifying passwords using the BCrypt algorithm.
 *
 * <p>This class provides secure password handling by generating salted hashes
 * and comparing raw passwords against stored hashes.</p>
 *
 */
public class PasswordHasher {

    /**
     * Hashes a plain text password using BCrypt with a generated salt.
     *
     * @param password the plain text password to hash
     * @return the hashed password
     * @throws IllegalArgumentException if the password is null or empty
     */
    public static String hashPassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        } else {
            String salt = BCrypt.gensalt(12);
            return BCrypt.hashpw(password, salt);
        }
    }

    /**
     * Compares a plain text password with a previously stored hash.
     *
     * @param inputPassword the password provided by the user
     * @param storedHash the hashed password stored in the database
     * @return true if the passwords match, false otherwise
     */
    public static boolean comparePasswords(String inputPassword, String storedHash) {
        return BCrypt.checkpw(inputPassword, storedHash);
    }
}
