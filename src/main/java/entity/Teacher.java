package entity;

import jakarta.persistence.*;

/**
 * Represents a Teacher entity in the system and database.
 */
@Entity
@Table(name = "teacher")
public class Teacher {

    /**
     * The unique identifier of the Teacher.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private int id;

    /**
     * The firstname of the Teacher in English.
     */
    @Column(name = "firstname_en")
    private String firstnameEN;

    /**
     * The firstname of the Teacher in Finnish.
     */
    @Column(name = "firstname_fi")
    private String firstnameFI;

    /**
     * The firstname of the Teacher in Japanese.
     */
    @Column(name = "firstname_ja")
    private String firstnameJA;

    /**
     * The firstname of the Teacher in Greek.
     */
    @Column(name = "firstname_el")
    private String firstnameEL;

    /**
     * The lastname of the Teacher in English.
     */
    @Column(name = "lastname_en")
    private String lastnameEN;

    /**
     * The lastname of the Teacher in Finnish.
     */
    @Column(name = "lastname_fi")
    private String lastnameFI;

    /**
     * The lastname of the Teacher in Japanese.
     */
    @Column(name = "lastname_ja")
    private String lastnameJA;

    /**
     * The lastname of the Teacher in Greeks.
     */
    @Column(name = "lastname_el")
    private String lastnameEL;

    /**
     * The email of the Teacher.
     */
    @Column(unique = true)
    private String email;

    /**
     * The password of the Teacher.
     */
    @Column(length = 500)
    private String password;

    /**
     * Constructs a Teacher with localized firstnames and lastnames, email and password.
     *
     * @param firstnameEN firstname in English
     * @param firstnameFI firstname in Finnish
     * @param firstnameJA firstname in Japanese
     * @param firstnameEL firstname in Greek
     * @param lastnameEN lastname in English
     * @param lastnameFI lastname in Finnish
     * @param lastnameJA lastname in Japanese
     * @param lastnameEL lastname in Greek
     * @param email teacher's email address
     * @param password teacher's password
     */
    public Teacher(String firstnameEN, String firstnameFI, String firstnameJA, String firstnameEL,
                   String lastnameEN, String lastnameFI, String lastnameJA, String lastnameEL,
                   String email, String password) {
        this.firstnameEN = firstnameEN;
        this.firstnameFI = firstnameFI;
        this.firstnameJA = firstnameJA;
        this.firstnameEL = firstnameEL;
        this.lastnameEN = lastnameEN;
        this.lastnameFI = lastnameFI;
        this.lastnameJA = lastnameJA;
        this.lastnameEL = lastnameEL;
        this.email = email;
        this.password = password;
    }

    /**
     * The default constructor for a Teacher.
     */
    public Teacher() {
    }

    /**
     * Gets the localized firstname of a Teacher.
     * @param lang the language code ("en", "fi", "ja", or "el")
     * @return localized firstname in the requested language, or English if the language is not supported
     */
    public String getFirstname(String lang) {
        String firstname;
        switch (lang) {
            case "en":
                firstname = this.firstnameEN;
                break;
            case "fi":
                firstname = this.firstnameFI;
                break;
            case "ja":
                firstname = this.firstnameJA;
                break;
            case "el":
                firstname = this.firstnameEL;
                break;
            default:
                firstname = this.firstnameEN;
        }
        return firstname;
    }

    /**
     * Gets the localized lastname of a Teacher.
     * @param lang the language code ("en", "fi", "ja", or "el")
     * @return localized lastname in the requested language, or English if the language is not supported
     */
    public String getLastname(String lang) {
        String lastname;
        switch (lang) {
            case "en":
                lastname = this.lastnameEN;
                break;
            case "fi":
                lastname = this.lastnameFI;
                break;
            case "ja":
                lastname = this.lastnameJA;
                break;
            case "el":
                lastname = this.lastnameEL;
                break;
            default:
                lastname = this.lastnameEN;
        }
        return lastname;
    }

    /**
     * Sets the localized firstname of a Teacher.
     * @param lang the language code ("en", "fi", "ja", or "el")
     */
    public void setFirstname(String lang, String firstname) {
        switch (lang) {
            case "en":
                this.firstnameEN = firstname;
                break;
            case "fi":
                this.firstnameFI = firstname;
                break;
            case "ja":
                this.firstnameJA = firstname;
                break;
            case "el":
                this.firstnameEL = firstname;
                break;
            default:
                this.firstnameEN = firstname;
        }
    }

    /**
     * Sets the localized lastname of a Teacher.
     * @param lang the language code ("en", "fi", "ja", or "el")
     */
    public void setLastname(String lang, String lastname) {
        switch (lang) {
            case "en":
                this.lastnameEN = lastname;
                break;
            case "fi":
                this.lastnameFI = lastname;
                break;
            case "ja":
                this.lastnameJA = lastname;
                break;
            case "el":
                this.lastnameEL = lastname;
                break;
            default:
                this.lastnameEN = lastname;
        }
    }

    /**
     * Gets the email of a Teacher object.
     * @return email of the Teacher instance
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of a Teacher object.
     * @param email the email to be set as String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of a Teacher object.
     * @return the password of the Teacher as String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of a Teacher object.
     * @param password the password to be set as String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the uniques id of a Teacher object.
     * @return the unique id of the Teacher as int
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id of a Teacher object.
     * @param id the id to be set as int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Overrides the toString method. Returns Teacher's id as a String.
     * @return the id of the Teacher as a String
     */
    @Override
    public String toString() {
        return Integer.toString(id);
    }


}
