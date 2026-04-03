package entity;
//PLACEHOLDER

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")

public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;
    @Column(name = "name_en")
    private String nameEN;
    @Column(name = "name_fi")
    private String nameFI;
    @Column(name = "name_ja")
    private String nameJA;
    @Column(name = "name_el")
    private String nameEL;
    @Column(unique = true)
    private String identifier;
    private String status;
    private LocalDateTime created;
    private LocalDateTime archived;
    @ManyToOne()
    @JoinColumn(name = "teacher_id", nullable = true)
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attends> attends = new ArrayList<>();

    public Course(String nameEN, String nameFI, String nameJA, String nameEL, String identifier, Teacher teacher) {
        this.nameEN = nameEN;
        this.nameFI = nameFI;
        this.nameJA = nameJA;
        this.nameEL = nameEL;
        this.identifier = identifier;
        this.status = "ACTIVE";
        this.created = LocalDateTime.now();
        this.teacher = teacher;
    }

    public Course() {
    }

    public String getName(String lang) {
        String name = null;
        switch (lang) {
            case "en":
                name = this.nameEN;
                break;
            case "fi":
                name = this.nameFI;
                break;
            case "ja":
                name = this.nameJA;
                break;
            case "el":
                name = this.nameEL;
                break;
        }
        return name;
    }

    public List<Attends> getAttends() {
        return attends;
    }

    public void setAttends(List<Attends> attends) {
        this.attends = attends;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getNameFI() {
        return nameFI;
    }

    public void setNameFI(String nameFI) {
        this.nameFI = nameFI;
    }

    public String getNameJA() {
        return nameJA;
    }

    public void setNameJA(String nameJA) {
        this.nameJA = nameJA;
    }

    public String getNameEL() {
        return nameEL;
    }

    public void setNameEL(String nameEL) {
        this.nameEL = nameEL;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return this.created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getArchived() {
        return this.archived;
    }

    public void setArchived(LocalDateTime archived) {
        this.archived = archived;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return this.nameEN;
    }


}
