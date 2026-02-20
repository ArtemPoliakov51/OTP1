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
    private String firstname;
    private String lastname;
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checks> checks = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attends> attends = new ArrayList<>();

    public Student(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
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

    public List<Checks> getChecks() {
        return checks;
    }

    public void setChecks(List<Checks> checks) {
        this.checks = checks;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
