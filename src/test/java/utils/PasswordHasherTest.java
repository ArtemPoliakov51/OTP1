package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    @DisplayName("PasswordHasher hashPassword() test")
    void hashPassword() {
        try {
            String password = "thisIsASuperSecretPassword";
            String hashed = PasswordHasher.hashPassword(password);

            assertNotNull(hashed);
            assertNotEquals(password, hashed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("PasswordHasher comparePasswords() successfully test")
    void compareCorrectPasswords() {
        try {
            String password = "thisIsASuperSecretPassword";
            String hashed = PasswordHasher.hashPassword(password);

            assertTrue(PasswordHasher.comparePasswords(password, hashed));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("PasswordHasher comparePasswords() unsuccessfully test")
    void compareIncorrectPasswords() {
        try {
            String password = "thisIsASuperSecretPassword";
            String fakePassword = "thisIsNotCorrectPassword";

            String hashed = PasswordHasher.hashPassword(password);

            assertFalse(PasswordHasher.comparePasswords(fakePassword, hashed));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("PasswordHasher hashPasswords() gives different hashes for same input test")
    void giveDifferentHashes() {
        try {
            String password = "thisIsASuperSecretPassword";

            String hashed = PasswordHasher.hashPassword(password);
            String hashed2 = PasswordHasher.hashPassword(password);

            assertNotEquals(hashed2, hashed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("PasswordHasher hashPasswords() with en empty and null strings test")
    void emptyOrNullPassword() {
        String password = "";
        assertThrows(Exception.class, () -> {
            PasswordHasher.hashPassword(password);
        });

        String password2 = null;
        assertThrows(Exception.class, () -> {
            PasswordHasher.hashPassword(password2);
        });
    }
}
