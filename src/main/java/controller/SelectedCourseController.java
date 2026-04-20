package controller;

import dao.*;
import entity.*;
import i18n.I18nManager;
import javafx.scene.layout.*;
import utils.PercentageCalculator;
import view.SelectedCourseView;
import java.util.List;

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
    private int getCourseAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        return PercentageCalculator.countCourseAttendancePercentage(attendanceChecks);
    }

    /**
     * Updates the view title using the course identifier.
     */
    public void updateViewTitle() {
        Course course = courseDao.find(courseId);
        courseView.displayViewTitle(course.getIdentifier());
    }

    /**
     * Displays course name, identifier, and overall attendance percentage in the view.
     */
    public void updateCourseInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Course course = courseDao.find(courseId);
        courseView.displayCourseNameAndIdentifier(course.getName(lang), course.getIdentifier());
        courseView.displayCourseAttendancePercentage(getCourseAttendancePercentage());
    }

    /**
     * Loads and displays all attendance checks for the selected course.
     */
    public void displayAttendanceChecks() {
        courseView.clearAttendanceChecksList();
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            courseView.addToAttendanceChecksList(attendanceCheck.getCheckDate(), attendanceCheck.getCheckTime(),
                    (int) PercentageCalculator.countAttendanceCheckPercentage(attendanceCheck), attendanceCheck.getId());
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
