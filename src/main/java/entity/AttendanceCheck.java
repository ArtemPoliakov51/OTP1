package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attendance_check")

public class AttendanceCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private int id;
    @Column(name = "check_date")
    private LocalDate checkDate;
    @Column(name = "check_time")
    private LocalTime checkTime;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "attendanceCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checks> checks = new ArrayList<>();

    public AttendanceCheck(Course course) {
        this.checkDate = LocalDate.now();
        this.checkTime = LocalTime.now();
        this.course = course;
    }

    public AttendanceCheck() {
    }

    public LocalDate getCheckDate() {
        return checkDate;
    }

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
