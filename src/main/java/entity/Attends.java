package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "attends")

public class Attends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attends_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;


    public Attends(Course course, Student student) {
        this.course = course;
        this.student = student;
    }

    public Attends() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
