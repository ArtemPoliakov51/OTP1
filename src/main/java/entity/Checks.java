package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "checks")
public class Checks {

    @EmbeddedId
    private ChecksId id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @ManyToOne
    @MapsId("attendanceCheckId")
    @JoinColumn(name = "attendance_check_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AttendanceCheck attendanceCheck;

    @Column(name = "attendance_status")
    private String attendanceStatus;

    @Column(length = 800)
    private String notes;


    public Checks(Student student, AttendanceCheck attendanceCheck) {
        this.student = student;
        this.attendanceCheck = attendanceCheck;
        this.id = new ChecksId(attendanceCheck.getId(), student.getId());
        this.attendanceStatus = "ABSENT";
        attendanceCheck.getChecks().add(this);
        student.getChecks().add(this);
    }

    public Checks() {
    }

    public ChecksId getId() {
        return id;
    }

    public void setId(ChecksId id) {
        this.id = id;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AttendanceCheck getAttendanceCheck() {
        return attendanceCheck;
    }

    public void setAttendanceCheck(AttendanceCheck attendanceCheck) {
        this.attendanceCheck = attendanceCheck;
    }
}
