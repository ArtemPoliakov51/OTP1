package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Represents an attendance record linking a Student to a specific AttendanceCheck.
 */
@Entity
@Table(name = "checks")
public class Checks {

    /**
     * Composite primary key for this entity.
     */
    @EmbeddedId
    private ChecksId id;

    /**
     * The Student associated with this attendance record.
     */
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    /**
     * The AttendanceCheck associated with this attendance record.
     */
    @ManyToOne
    @MapsId("attendanceCheckId")
    @JoinColumn(name = "attendance_check_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AttendanceCheck attendanceCheck;

    /**
     * The attendance status of a Student at a certain AttendanceCheck ("PRESENT", "ABSENT" or "EXCUSED").
     */
    @Column(name = "attendance_status")
    private String attendanceStatus;

    /**
     * The optional notes aimed towards a Student at a certain AttendanceCheck.
     */
    @Column(length = 800)
    private String notes;

    /**
     * Constructs a Checks attendance record with Student and AttendanceCheck objects.
     * Creates and sets the unique composite key.
     * Sets the attendance status as "ABSENT" by default.
     * Adds this Checks object to Student's and AttendanceCheck's Checks lists.
     *
     * @param student the Student associated with the attendance record
     * @param attendanceCheck the AttendanceCheck associated with the attendance record
     */
    public Checks(Student student, AttendanceCheck attendanceCheck) {
        this.student = student;
        this.attendanceCheck = attendanceCheck;
        this.id = new ChecksId(attendanceCheck.getId(), student.getId());
        this.attendanceStatus = "ABSENT";
        attendanceCheck.getChecks().add(this);
        student.getChecks().add(this);
    }

    /**
     * The default constructor for a Checks object.
     */
    public Checks() {
    }

    /**
     * Gets the uniques composite id of a Checks object.
     * @return the unique composite id of the Checks as ChecksId object
     */
    public ChecksId getId() {
        return id;
    }

    /**
     * Sets the unique composite id of a Checks object.
     * @param id the unique composite id to be set as a ChecksId object
     */
    public void setId(ChecksId id) {
        this.id = id;
    }

    /**
     * Gets the attendance status of a Student associated with a Checks attendance record ("PRESENT", "ABSENT" or "EXCUSED").
     * @return the attendance status of the Student as a String
     */
    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    /**
     * Sets the attendance status of a Student associated with a Checks attendance record ("PRESENT", "ABSENT" or "EXCUSED").
     * @param attendanceStatus the attendance status of the Student as a String
     */
    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    /**
     * Gets the notes aimed towards a Student associated with a Checks attendance record.
     * @return the notes aimed towards the Student
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes aimed towards a Student associated with a Checks attendance record.
     * @param notes the notes to be set as s String
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the Student object associated with a Checks attendance record.
     * @return the Student associated with the Checks object
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Sets the Student object associated with a Checks attendance record.
     * @param student the Student object to be set
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Gets the AttendanceCheck object associated with a Checks attendance record.
     * @return the AttendanceCheck associated with the Checks object
     */
    public AttendanceCheck getAttendanceCheck() {
        return attendanceCheck;
    }

    /**
     * Sets the AttendanceCheck object associated with a Checks attendance record.
     * @param attendanceCheck the AttendanceCheck object to be set
     */
    public void setAttendanceCheck(AttendanceCheck attendanceCheck) {
        this.attendanceCheck = attendanceCheck;
    }
}
