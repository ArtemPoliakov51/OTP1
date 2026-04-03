package entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int id;
    @Column(name = "firstname_en")
    private String firstnameEN;
    @Column(name = "firstname_fi")
    private String firstnameFI;
    @Column(name = "firstname_ja")
    private String firstnameJA;
    @Column(name = "firstname_el")
    private String firstnameEL;
    @Column(name = "lastname_en")
    private String lastnameEN;
    @Column(name = "lastname_fi")
    private String lastnameFI;
    @Column(name = "lastname_ja")
    private String lastnameJA;
    @Column(name = "lastname_el")
    private String lastnameEL;
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checks> checks = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attends> attends = new ArrayList<>();

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

    public Student() {
    }

    public List<Attends> getAttends() {
        return attends;
    }

    public void setAttends(List<Attends> attends) {
        this.attends = attends;
    }

    public String getFirstname(String lang) {
        String firstname = null;
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
        }
        return firstname;
    }

    public String getLastname(String lang) {
        String lastname = null;
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
        }
        return lastname;
    }

    public String getFirstnameEN() {
        return firstnameEN;
    }

    public void setFirstnameEN(String firstnameEN) {
        this.firstnameEN = firstnameEN;
    }

    public String getFirstnameFI() {
        return firstnameFI;
    }

    public void setFirstnameFI(String firstnameFI) {
        this.firstnameFI = firstnameFI;
    }

    public String getFirstnameJA() {
        return firstnameJA;
    }

    public void setFirstnameJA(String firstnameJA) {
        this.firstnameJA = firstnameJA;
    }

    public String getFirstnameEL() {
        return firstnameEL;
    }

    public void setFirstnameEL(String firstnameEL) {
        this.firstnameEL = firstnameEL;
    }

    public String getLastnameEN() {
        return lastnameEN;
    }

    public void setLastnameEN(String lastnameEN) {
        this.lastnameEN = lastnameEN;
    }

    public String getLastnameFI() {
        return lastnameFI;
    }

    public void setLastnameFI(String lastnameFI) {
        this.lastnameFI = lastnameFI;
    }

    public String getLastnameJA() {
        return lastnameJA;
    }

    public void setLastnameJA(String lastnameJA) {
        this.lastnameJA = lastnameJA;
    }

    public String getLastnameEL() {
        return lastnameEL;
    }

    public void setLastnameEL(String lastnameEL) {
        this.lastnameEL = lastnameEL;
    }

    public List<Checks> getChecks() {
        return checks;
    }

    public void setChecks(List<Checks> checks) {
        this.checks = checks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return Integer.toString(id);
    }


}
