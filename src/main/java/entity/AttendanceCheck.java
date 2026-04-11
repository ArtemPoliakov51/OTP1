package entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AttendanceCheck entity in the system and database.
 */
@Entity
@Table(name = "attendance_check")
public class AttendanceCheck {

    /**
     * The unique identifier of the AttendanceCheck.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private int id;

    /**
     * The creation date of the AttendanceCheck.
     */
    @Column(name = "check_date")
    private LocalDate checkDate;

    /**
     * The creation time of the AttendanceCheck.
     */
    @Column(name = "check_time")
    private LocalTime checkTime;

    /**
     * The Course entity to which the AttendanceCheck belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    /**
     * The Checks instances associated with the AttendanceCheck.
     */
    @OneToMany(mappedBy = "attendanceCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checks> checks = new ArrayList<>();

    /**
     * Constructs an AttendanceCheck with a Course.
     * Sets the creation date and time attributes with current date and time.
     *
     * @param course the Course entity to which the AttendanceCheck belongs to
     */
    public AttendanceCheck(Course course) {
        this.checkDate = LocalDate.now();
        this.checkTime = LocalTime.now();
        this.course = course;
    }

    /**
     * The default constructor for an AttendanceCheck.
     */
    public AttendanceCheck() {
    }

    /**
     * Gets the creation date of an AttendanceChek object.
     * @return the creation date of the AttendanceCheck as a LocalDate object
     */
    public LocalDate getCheckDate() {
        return checkDate;
    }

    /**
     * Sets the creation date of an AttendanceCheck object.
     * @param checkDate the creation date to be set as a LocalDate object
     */
    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public List<Checks> getChecks() {
        return checks;
    }

    public void setChecks(List<Checks> checks) {
        this.checks = checks;
    }

    public LocalTime getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(LocalTime checkTime) {
        this.checkTime = checkTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
