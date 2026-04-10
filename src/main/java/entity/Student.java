package entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student entity in the system and database.
 */

@Entity
@Table(name = "student")
public class Student {

    /**
     * The unique identifier of the student.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int id;

    /**
     * The firstname of the student in English.
     */
    @Column(name = "firstname_en")
    private String firstnameEN;

    /**
     * The firstname of the student in Finnish.
     */
    @Column(name = "firstname_fi")
    private String firstnameFI;

    /**
     * The firstname of the student in Japanese.
     */
    @Column(name = "firstname_ja")
    private String firstnameJA;

    /**
     * The firstname of the student in Greek.
     */
    @Column(name = "firstname_el")
    private String firstnameEL;

    /**
     * The lastname of the student in English.
     */
    @Column(name = "lastname_en")
    private String lastnameEN;

    /**
     * The lastname of the student in Finnish.
     */
    @Column(name = "lastname_fi")
    private String lastnameFI;

    /**
     * The lastname of the student in Japanese.
     */
    @Column(name = "lastname_ja")
    private String lastnameJA;

    /**
     * The lastname of the student in Greek.
     */
    @Column(name = "lastname_el")
    private String lastnameEL;

    /**
     * The email of the student.
     */
    private String email;

    /**
     * The Checks instances associated with the student.
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checks> checks = new ArrayList<>();

    /**
     * The Attends instances associated with the student.
     */
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attends> attends = new ArrayList<>();

    /**
     * Constructs a Student with localized firstnames and lastnames and email.
     *
     * @param firstnameEN first name in English
     * @param firstnameFI first name in Finnish
     * @param firstnameJA first name in Japanese
     * @param firstnameEL first name in Greek
     * @param lastnameEN last name in English
     * @param lastnameFI last name in Finnish
     * @param lastnameJA last name in Japanese
     * @param lastnameEL last name in Greek
     * @param email student's email address
     */
    public Student(String firstnameEN, String firstnameFI, String firstnameJA, String firstnameEL, String lastnameEN, String lastnameFI, String lastnameJA, String lastnameEL, String email) {
        this.firstnameEN = firstnameEN;
        this.firstnameFI = firstnameFI;
        this.firstnameJA = firstnameJA;
        this.firstnameEL = firstnameEL;
        this.lastnameEN = lastnameEN;
        this.lastnameFI = lastnameFI;
        this.lastnameJA = lastnameJA;
        this.lastnameEL = lastnameEL;
        this.email = email;
    }

    /**
     * The default constructor for Student class.
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
     * Gets the localized lastname of a Student.
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
     * Sets the localized firstname of a Student.
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
     * Sets the localized lastname of a Student.
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
        return email;
    }

    /**
     * Sets the email of a Student object.
     * @param email the email to be set as String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the uniques id of a Student object.
     * @return the unique id of the student as int
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
