package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "attends")
public class Attends {

    @EmbeddedId
    private AttendsId id;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;


    public Attends(Course course, Student student) {
        this.course = course;
        this.student = student;
        this.id = new AttendsId(course.getId(), student.getId());
        course.getAttends().add(this);
        student.getAttends().add(this);
    }

    public Attends() {
    }

    public AttendsId getId() {
        return id;
    }

    public void setId(AttendsId id) {
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
