package entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Student entity in the system and database.
 */
@Entity
@Table(name = "student")
public class Student {

    /**
     * The unique identifier of the Student.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int id;

    /**
     * The firstname of the Student in English.
     */
    @Column(name = "firstname_en")
    private String studentFirstnameEN;

    /**
     * The firstname of the Student in Finnish.
     */
    @Column(name = "firstname_fi")
    private String studentFirstnameFI;

    /**
     * The firstname of the Student in Japanese.
     */
    @Column(name = "firstname_ja")
    private String studentFirstnameJA;

    /**
     * The firstname of the student in Greek.
     */
    @Column(name = "firstname_el")
    private String studentFirstnameEL;

    /**
     * The lastname of the Student in English.
     */
    @Column(name = "lastname_en")
    private String studentLastnameEN;

    /**
     * The lastname of the Student in Finnish.
     */
    @Column(name = "lastname_fi")
    private String studentLastnameFI;

    /**
     * The lastname of the Student in Japanese.
     */
    @Column(name = "lastname_ja")
    private String studentLastnameJA;

    /**
     * The lastname of the Student in Greek.
     */
    @Column(name = "lastname_el")
    private String studentLastnameEL;

    /**
     * The email of the Student.
     */
    @Column(name = "email")
    private String studentEmail;

    /**
     * The Checks instances associated with the Student.
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checks> checks = new ArrayList<>();

    /**
     * The Attends instances associated with the Student.
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attends> attends = new ArrayList<>();

    /**
     * Constructs a Student with localized firstnames and lastnames and email.
     *
     * @param firstnameEN firstname in English
     * @param firstnameFI firstname in Finnish
     * @param firstnameJA firstname in Japanese
     * @param firstnameEL firstname in Greek
     * @param lastnameEN lastname in English
     * @param lastnameFI lastname in Finnish
     * @param lastnameJA lastname in Japanese
     * @param lastnameEL lastname in Greek
     * @param email student's email address
     */
    public Student(String firstnameEN, String firstnameFI, String firstnameJA, String firstnameEL, String lastnameEN, String lastnameFI, String lastnameJA, String lastnameEL, String email) {
        this.studentFirstnameEN = firstnameEN;
        this.studentFirstnameFI = firstnameFI;
        this.studentFirstnameJA = firstnameJA;
        this.studentFirstnameEL = firstnameEL;
        this.studentLastnameEN = lastnameEN;
        this.studentLastnameFI = lastnameFI;
        this.studentLastnameJA = lastnameJA;
        this.studentLastnameEL = lastnameEL;
        this.studentEmail = email;
    }

    /**
     * The default constructor for a Student.
     */
    public Student() {
    }

    /**
     * Gets the Attends instances associated with Student object.
     * @return list of Attends instances
     */
    public List<Attends> getAttends() {
        return attends;
    }

    /**
     * Sets the Attends instances associated with Student object.
     * @param attends a list of Attends instances
     */
    public void setAttends(List<Attends> attends) {
        this.attends = attends;
    }

    /**
     * Gets the localized firstname of a Student.
     * @param lang the language code ("en", "fi", "ja", or "el")
     * @return localized firstname in the requested language, or English if the language is not supported
     */
    public String getFirstname(String lang) {
        String firstname;
        switch (lang) {
            case "en":
                firstname = this.studentFirstnameEN;
                break;
            case "fi":
                firstname = this.studentFirstnameFI;
                break;
            case "ja":
                firstname = this.studentFirstnameJA;
                break;
            case "el":
                firstname = this.studentFirstnameEL;
                break;
            default:
                firstname = this.studentFirstnameEN;
        }
        return firstname;
    }

    /**
     * Gets the localized lastname of a Student.
     * @param lang the language code ("en", "fi", "ja", or "el")
     * @return localized lastname in the requested language, or English if the language is not supported
     */
    public String getLastname(String lang) {
        String lastname;
        switch (lang) {
            case "en":
                lastname = this.studentLastnameEN;
                break;
            case "fi":
                lastname = this.studentLastnameFI;
                break;
            case "ja":
                lastname = this.studentLastnameJA;
                break;
            case "el":
                lastname = this.studentLastnameEL;
                break;
            default:
                lastname = this.studentLastnameEN;
        }
        return lastname;
    }

    /**
     * Sets the localized firstname of a Student.
     * @param lang the language code ("en", "fi", "ja", or "el")
     */
    public void setFirstname(String lang, String firstname) {
        switch (lang) {
            case "en":
                this.studentFirstnameEN = firstname;
                break;
            case "fi":
                this.studentFirstnameFI = firstname;
                break;
            case "ja":
                this.studentFirstnameJA = firstname;
                break;
            case "el":
                this.studentFirstnameEL = firstname;
                break;
            default:
                this.studentFirstnameEN = firstname;
        }
    }

    /**
     * Sets the localized lastname of a Student.
     * @param lang the language code ("en", "fi", "ja", or "el")
     */
    public void setLastname(String lang, String lastname) {
        switch (lang) {
            case "en":
                this.studentLastnameEN = lastname;
                break;
            case "fi":
                this.studentLastnameFI = lastname;
                break;
            case "ja":
                this.studentLastnameJA = lastname;
                break;
            case "el":
                this.studentLastnameEL = lastname;
                break;
            default:
                this.studentLastnameEN = lastname;
        }
    }

    /**
     * Gets the Checks instances associated with Student object.
     * @return list of Checks instances
     */
    public List<Checks> getChecks() {
        return checks;
    }

    /**
     * Sets the Checks instances associated with Student object.
     * @param checks a list of Checks instances
     */
    public void setChecks(List<Checks> checks) {
        this.checks = checks;
    }

    /**
     * Gets the email of a Student object.
     * @return email of the Student instance
     */
    public String getEmail() {
        return studentEmail;
    }

    /**
     * Sets the email of a Student object.
     * @param email the email to be set as String
     */
    public void setEmail(String email) {
        this.studentEmail = email;
    }

    /**
     * Gets the uniques id of a Student object.
     * @return the unique id of the Student as int
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets the id of a Student object.
     * @param id the id to be set as int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Overrides the toString method. Returns Student's id as a String.
     * @return the id of the Student as a String
     */
    @Override
    public String toString() {
        return Integer.toString(id);
    }


}
