package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Represents an enrollment record linking a Student to a specific Course.
 */
@Entity
@Table(name = "attends")
public class Attends {

    /**
     * Composite primary key for this entity.
     */
    @EmbeddedId
    private AttendsId id;

    /**
     * The Course associated with this enrollment record.
     */
    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    /**
     * The Student associated with this enrollment record.
     */
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;


    /**
     * Constructs an Attends enrollment record with Student and Course objects.
     * Creates and sets the unique composite key.
     * Adds this Attends object to Student's and Course's Attends lists.
     *
     * @param course the AttendanceCheck associated with the attendance record
     * @param student the Student associated with the attendance record
     */
    public Attends(Course course, Student student) {
        this.course = course;
        this.student = student;
        this.id = new AttendsId(course.getId(), student.getId());
        course.getAttends().add(this);
        student.getAttends().add(this);
    }

    /**
     * The default constructor for an Attends object.
     */
    public Attends() {
    }

    /**
     * Gets the uniques composite id of an Attends object.
     * @return the unique composite id of the Attends as AttendsId object
     */
    public AttendsId getId() {
        return id;
    }

    /**
     * Sets the unique composite id of an Attends object.
     * @param id the unique composite id to be set as an AttendsId object
     */
    public void setId(AttendsId id) {
        this.id = id;
    }

    /**
     * Gets the Course object associated with an Attends enrollment record.
     * @return the Course associated with the Attends object
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the Course object associated with an Attends enrollment record.
     * @param course the Course object to be set
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Gets the Student object associated with an Attends enrollment record.
     * @return the Student associated with the Attends object
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Sets the Student object associated with an Attends enrollment record.
     * @param student the Student object to be set
     */
    public void setStudent(Student student) {
        this.student = student;
    }
}
