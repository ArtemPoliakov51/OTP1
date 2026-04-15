package controller;

import dao.AttendanceCheckDao;
import dao.ChecksDao;
import dao.CourseDao;
import dao.TeacherDao;
import entity.*;
import i18n.I18nManager;
import utils.PercentageCalculator;
import view.SelectedAttendanceCheckView;
import view.SelectedCourseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for managing a single attendance check view.
 *
 * <p>This class handles displaying attendance check details such as date, time,
 * student attendance statuses, and calculated attendance percentage.</p>
 *
 * <p>It also allows updating student attendance status (present/absent/excused)
 * and saving notes for individual students.</p>
 */
public class SelectedAttendanceCheckController {

    /** The course associated with this attendance check. */
    private final Course course;

    /** DAO used for accessing course data. */
    private final CourseDao courseDao = new CourseDao();

    /** View responsible for displaying a single attendance check. */
    private final SelectedAttendanceCheckView attCheckView;

    /** ID of the attendance check being managed. */
    private final int attendanceCheckId;

    /** DAO used for accessing attendance check data. */
    private final AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    /** DAO used for accessing and updating student attendance records. */
    private final ChecksDao checksDao = new ChecksDao();

    /** ID of the currently logged-in teacher. */
    private final int teacherId;

    /**
     * Constructs a new SelectedAttendanceCheckController.
     *
     * @param attCheckView the view used to display attendance check information
     * @param attendanceCheckId the ID of the attendance check
     * @param courseId the ID of the course associated with the attendance check
     */
    public SelectedAttendanceCheckController(SelectedAttendanceCheckView attCheckView, int attendanceCheckId, int courseId) {
        this.course = courseDao.find(courseId);
        this.attendanceCheckId = attendanceCheckId;
        this.attCheckView = attCheckView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Updates the view title using the course identifier.
     */
    public void updateViewTitle() {
        attCheckView.displayViewTitle(course.getIdentifier());
    }


    /**
     * Displays attendance check date, time, and calculated attendance percentage.
     */
    public void updateCheckInfo() {
        AttendanceCheck attendanceCheck = attendanceCheckDao.find(attendanceCheckId);
        attCheckView.displayChecksDateAndTime(
                attendanceCheck.getCheckDate(), attendanceCheck.getCheckTime());
        attCheckView.displayChecksAttendancePercentage(
                (int) PercentageCalculator.countAttendanceCheckPercentage(attendanceCheck));
    }

    /**
     * Loads and displays all students and their attendance statuses for this check.
     */
    public void displayStudents() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        attCheckView.clearStudentsList();
        List<Checks> checks = checksDao.findByAttendanceCheck(attendanceCheckId);
        for (Checks aChecks : checks) {
            Student student = aChecks.getStudent();
            attCheckView.addToStudentsList(student.getFirstname(lang), student.getLastname(lang), student.getId(), aChecks.getAttendanceStatus(), aChecks.getNotes(), course.getId());
        }
    }

    /**
     * Marks a student as absent or excused and updates the attendance percentage.
     *
     * @param studentId the ID of the student
     * @param isExcused true if absence is excused, false if unexcused
     */
    public void updateAbsenceStatus(int studentId, boolean isExcused) {
        AttendanceCheck attendanceCheck = attendanceCheckDao.find(attendanceCheckId);
        Checks checks = checksDao.find(attendanceCheckId, studentId);
        checks.setAttendanceStatus(isExcused ? "EXCUSED" : "ABSENT");
        checksDao.update(checks);
        attCheckView.displayChecksAttendancePercentage(
                (int) PercentageCalculator.countAttendanceCheckPercentage(attendanceCheck));
    }

    /**
     * Updates a student's attendance status to present or absent.
     *
     * @param studentId the ID of the student
     * @param isPresent true if student is present, false if absent
     */
    public void updateStudentStatus(int studentId, boolean isPresent) {
        AttendanceCheck attendanceCheck = attendanceCheckDao.find(attendanceCheckId);
        Checks checks = checksDao.find(attendanceCheckId, studentId);
        System.out.println(checks);
        checks.setAttendanceStatus(isPresent ? "PRESENT" : "ABSENT");
        checksDao.update(checks);
        attCheckView.displayChecksAttendancePercentage(
                (int) PercentageCalculator.countAttendanceCheckPercentage(attendanceCheck));
    }

    /**
     * Saves or updates a note for a specific student's attendance record.
     *
     * @param studentId the ID of the student
     * @param note the note to be saved
     */
    public void saveNote(int studentId, String note) {
        Checks checks = checksDao.find(attendanceCheckId, studentId);
        checks.setNotes(note);
        checksDao.update(checks);
    }

}
