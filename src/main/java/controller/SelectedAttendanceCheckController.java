package controller;

import dao.AttendanceCheckDao;
import dao.ChecksDao;
import dao.CourseDao;
import entity.*;
import view.SelectedAttendanceCheckView;
import view.SelectedCourseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectedAttendanceCheckController {

    /** The Course entity for course data */
    private Course course;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The SelectedCourseView class instance */
    private SelectedAttendanceCheckView attCheckView;
    /** The AttendanceCheck entity for attendance check data */
    private AttendanceCheck attendanceCheck;
    /** The AttendanceCheckDao class instance for database operations on the attendance_check table */
    private AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    private ChecksDao checksDao = new ChecksDao();

    /**
     * Constructor for SelectedAttendanceCheckController
     * @param attCheckView The instance of the SelectedCourseView class
     * @param courseId The unique ID of the course
     */
    public SelectedAttendanceCheckController(SelectedAttendanceCheckView attCheckView, int attendanceCheckId, int courseId) {
        this.course = courseDao.find(courseId);
        this.attendanceCheck = attendanceCheckDao.find(attendanceCheckId);
        this.attCheckView = attCheckView;
    }

    /**
     * Method for counting the attendance percentage for a single attendance check
     * @param attCheck The instance of the AttendanceCheck class
     * @return attendance percentage for single attendance check
     */
    private int countAttendancePercentage(AttendanceCheck attCheck) {
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
        return  (int) ((double) present.size() / (double) checks.size() * 100);
    }

    /**
     * Method for passing the course's unique identifier for the view
     */
    public void updateViewTitle() {
        attCheckView.displayViewTitle(course.getIdentifier());
    }

    /**
     * Method for passing the attendance check's creation date and time and the attendance percentage for the view
     */
    public void updateCheckInfo() {
        attCheckView.displayChecksDateAndTime(attendanceCheck.getCheckDate().toString(), attendanceCheck.getCheckTime().toString());
        attCheckView.displayChecksAttendancePercentage(countAttendancePercentage(attendanceCheck));
    }

    /**
     * Method for finding and passing the course's students' info for the view
     */
    public void displayStudents() {
        attCheckView.clearStudentsList();
        List<Checks> checks = checksDao.findByAttendanceCheck(attendanceCheck);
        for (Checks aChecks : checks) {
            Student student = aChecks.getStudent();
            attCheckView.addToStudentsList(student.getFirstname(), student.getLastname(), student.getId(), aChecks.getAttendanceStatus(), aChecks.getNotes(), aChecks.getId());
        }
    }

    public void updateAbsenceStatus(int checksId, String currentStatus) {
        Checks checks = checksDao.find(checksId);
        checks.setAttendanceStatus(currentStatus.equals("ABSENT") ? "EXCUSED" : "ABSENT");
        checksDao.update(checks);
        attCheckView.displayChecksAttendancePercentage(countAttendancePercentage(attendanceCheck));
    }

    public void updateStudentStatus(int checksId, boolean isPresent) {
        Checks checks = checksDao.find(checksId);
        checks.setAttendanceStatus(isPresent ? "PRESENT" : "ABSENT");
        checksDao.update(checks);
        attCheckView.displayChecksAttendancePercentage(countAttendancePercentage(attendanceCheck));
    }

    public void saveNote(int checksId, String note) {
        Checks checks = checksDao.find(checksId);
        checks.setNotes(note);
        checksDao.update(checks);
    }

}
