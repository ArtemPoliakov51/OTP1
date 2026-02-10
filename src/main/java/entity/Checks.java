package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "checks")

public class Checks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checks_id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "attendance_check_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AttendanceCheck attendanceCheck;
    @Column(name = "attendance_status")
    private String attendanceStatus;
    @Column(length = 800)
    private String notes;


    public Checks(Student student, AttendanceCheck attendanceCheck) {
        this.student = student;
        this.attendanceCheck = attendanceCheck;
        this.attendanceStatus = "ABSENT";
    }

    public Checks() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
