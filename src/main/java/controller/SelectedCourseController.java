package controller;

import dao.AttendanceCheckDao;
import dao.ChecksDao;
import dao.CourseDao;
import entity.AttendanceCheck;
import entity.Checks;
import entity.Course;
import javafx.scene.layout.*;
import view.SelectedCourseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SelectedCourseController {

    private Course course;
    private CourseDao courseDao = new CourseDao();
    private SelectedCourseView courseView;
    private AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    public SelectedCourseController(SelectedCourseView courseView, int courseId) {
        this.course = courseDao.find(courseId);
        this.courseView = courseView;
    }

    private int countCourseAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);

        List<Double> attPercentages = new ArrayList<>();
        for (AttendanceCheck attCheck : attendanceChecks) {
            attPercentages.add(countAttendanceCheckPercentage(attCheck));
        }
        double total = 0;
        for (Double percentage : attPercentages) {
            System.out.println(percentage);
            total = total + percentage;
        }
        return (int) total/attPercentages.size();
    }

    private double countAttendanceCheckPercentage(AttendanceCheck attCheck) {
        ChecksDao checksDao = new ChecksDao();
        List<Checks> checks = checksDao.findByAttendanceCheck(attCheck);
        // Go through all of them, and if student was present add it to a new list
        List<Checks> present = new ArrayList<>();
        for (Checks checksCheck : checks) {
            if (Objects.equals(checksCheck.getAttendanceStatus(), "PRESENT")) {
                System.out.println("Present!");
                present.add(checksCheck);
            }
        }
        // Count the attendance percentage for this attendance check
        System.out.println(present.size());
        System.out.println(checks.size());
        System.out.println(present.size() / checks.size());
        double attCheckPercentage = (double) present.size() / (double) checks.size() * 100;
        return attCheckPercentage;
    }

    public void updateViewTitle() {
        courseView.displayViewTitle(course.getIdentifier());
    }

    public void updateCourseInfo() {
        courseView.displayCourseNameAndIdentifier(course.getName(), course.getIdentifier());
        courseView.displayCourseAttendancePercentage(countCourseAttendancePercentage());
    }

    public void displayAttendanceChecks() {
        courseView.clearAttendanceChecksList();
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            courseView.addToAttendanceChecksList(attendanceCheck.getCheckDate(), attendanceCheck.getCheckTime(),
                    (int) countAttendanceCheckPercentage(attendanceCheck), attendanceCheck.getId());
        }
    }
}
