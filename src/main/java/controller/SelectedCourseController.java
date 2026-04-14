package controller;

import dao.*;
import entity.*;
import i18n.I18nManager;
import javafx.scene.layout.*;
import view.SelectedCourseView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for managing a selected course view.
 *
 * <p>This class handles displaying course information, attendance checks,
 * attendance statistics, and managing attendance check creation and deletion.</p>
 *
 * <p>It also communicates with DAO classes to retrieve and update course-related data.</p>
 */
public class SelectedCourseController {

    /** The ID of the selected course. */
    private final int courseId;

    /** DAO used for accessing course data from the database. */
    private final CourseDao courseDao = new CourseDao();

    /** View responsible for displaying selected course information. */
    private final SelectedCourseView courseView;

    /** DAO used for accessing attendance check data. */
    private final AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    /** The ID of the currently logged-in teacher. */
    private final int teacherId;

    /**
     * Constructs a new SelectedCourseController.
     *
     * @param courseView the view used to display course details
     * @param courseId the ID of the selected course
     */
    public SelectedCourseController(SelectedCourseView courseView, int courseId) {
        this.courseId = courseId;
        this.courseView = courseView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Calculates the overall attendance percentage for the selected course.
     *
     * <p>The value is computed as the average of all attendance check percentages.</p>
     *
     * @return overall course attendance percentage
     */
    private int countCourseAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

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
     * Calculates attendance percentage for a single attendance check.
     *
     * @param attCheck the attendance check to evaluate
     * @return attendance percentage (0–100)
     */
    private double countAttendanceCheckPercentage(AttendanceCheck attCheck) {
        ChecksDao checksDao = new ChecksDao();
        List<Checks> checks = checksDao.findByAttendanceCheck(attCheck.getId());
        // Go through all of them, and if student was present add it to a new list
        List<Checks> present = new ArrayList<>();
        for (Checks checksCheck : checks) {
            if (Objects.equals(checksCheck.getAttendanceStatus(), "PRESENT")) {
                System.out.println("Present!");
                present.add(checksCheck);
            }
        }
        // Count the attendance percentage for this attendance check
        double attCheckPercentage;
        if (!checks.isEmpty()) {
            attCheckPercentage = (double) present.size() / (double) checks.size() * 100;
        } else {
            attCheckPercentage = 0;
        }
        return attCheckPercentage;
    }


    /**
     * Updates the view title using the course identifier.
     */
    public void updateViewTitle() {
        Course course = courseDao.find(courseId);
        courseView.displayViewTitle(course.getIdentifier());
    }

    /**
     * Retrieves and displays information about the currently logged-in teacher.
     */
    public void showTeacherInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        courseView.displayTeacherInfo(teacher.getFirstname(lang), teacher.getLastname(lang), teacher.getEmail());
    }

    /**
     * Displays course name, identifier, and overall attendance percentage in the view.
     */
    public void updateCourseInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Course course = courseDao.find(courseId);
        courseView.displayCourseNameAndIdentifier(course.getName(lang), course.getIdentifier());
        courseView.displayCourseAttendancePercentage(countCourseAttendancePercentage());
    }

    /**
     * Loads and displays all attendance checks for the selected course.
     */
    public void displayAttendanceChecks() {
        courseView.clearAttendanceChecksList();
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            courseView.addToAttendanceChecksList(attendanceCheck.getCheckDate(), attendanceCheck.getCheckTime(),
                    (int) countAttendanceCheckPercentage(attendanceCheck), attendanceCheck.getId());
        }
    }

    /**
     * Deletes an attendance check from the course.
     *
     * @param attCheckId the ID of the attendance check to delete
     */
    public void deleteAttendanceCheck(int attCheckId) {
        System.out.println("Delete " + attCheckId);
        attendanceCheckDao.delete(attCheckId);
    }

    /**
     * Creates a new attendance check for the course and initializes
     * attendance records for all enrolled students.
     */
    public void createNewAttendanceCheck() {
        int id = attendanceCheckDao.persist(courseId);

        AttendsDao attendsDao = new AttendsDao();
        List<Attends> courseAttends = attendsDao.findByCourse(courseId);

        ChecksDao checksDao = new ChecksDao();
        for (Attends anAttends : courseAttends) {
            checksDao.persist(id, anAttends.getStudent().getId());
        }
    }
}
