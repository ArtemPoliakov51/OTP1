package entity;
//PLACEHOLDER

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "course")

public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;
    private String name;
    private String identifier;
    private String status;
    private LocalDateTime created;
    private LocalDateTime archived;
    @ManyToOne()
    @JoinColumn(name = "teacher_id", nullable = true)
    private Teacher teacher;


    public Course(String name, String identifier, Teacher teacher) {
        this.name = name;
        this.identifier = identifier;
        this.status = "ACTIVE";
        this.created = LocalDateTime.now();
        this.teacher = teacher;
    }

    public Course() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
        return this.name;
    }


}
