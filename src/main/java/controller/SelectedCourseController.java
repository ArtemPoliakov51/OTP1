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

/**
 * SelectedCourseController class, for communication between a SelectedCourseView and dao and entity classes
 * @version 1.0
 */
public class SelectedCourseController {

    /** The Course entity for course data */
    private Course course;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The SelectedCourseView class instance */
    private SelectedCourseView courseView;
    /** The AttendanceCheckDao class instance for database operations on the attendance_check table */
    private AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    /**
     * Constructor for SelectedCourseController
     * @param courseView The instance of the SelectedCourseView class
     * @param courseId The unique ID of the course
     */
    public SelectedCourseController(SelectedCourseView courseView, int courseId) {
        this.course = courseDao.find(courseId);
        this.courseView = courseView;
    }

    /**
     * Method for counting the overall attendance percentage for the course
     * @return the total attendance percentage for single course
     */
    private int countCourseAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);

        List<Double> attPercentages = new ArrayList<>();
        for (AttendanceCheck attCheck : attendanceChecks) {
            attPercentages.add(countAttendanceCheckPercentage(attCheck));
        }
        int totalAttendancePercentage = 0;

        if (attPercentages.size() != 0) {
            double total = 0;
            for (Double percentage : attPercentages) {
                total = total + percentage;
            }
            totalAttendancePercentage = (int) total/attPercentages.size();
        }

        return totalAttendancePercentage;
    }

    /**
     * Method for counting the attendance percentage for a single attendance check
     * @param attCheck The instance of the AttendanceCheck class
     * @return attendance percentage for single attendance check
     */
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

    /**
     * Method for passing the course's unique identifier for the view
     */
    public void updateViewTitle() {
        courseView.displayViewTitle(course.getIdentifier());
    }

    /**
     * Method for passing the course name and identifier info and course's attendance percentage for the view
     */
    public void updateCourseInfo() {
        courseView.displayCourseNameAndIdentifier(course.getName(), course.getIdentifier());
        courseView.displayCourseAttendancePercentage(countCourseAttendancePercentage());
    }

    /**
     * Method for finding and passing the course's attendance checks' info for the view
     */
    public void displayAttendanceChecks() {
        courseView.clearAttendanceChecksList();
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            courseView.addToAttendanceChecksList(attendanceCheck.getCheckDate(), attendanceCheck.getCheckTime(),
                    (int) countAttendanceCheckPercentage(attendanceCheck), attendanceCheck.getId());
        }
    }

    public void deleteAttendanceCheck(int attCheckId) {
        System.out.println("Delete " + attCheckId);
        AttendanceCheck found = attendanceCheckDao.find(attCheckId);
        attendanceCheckDao.delete(found);
    }
}
