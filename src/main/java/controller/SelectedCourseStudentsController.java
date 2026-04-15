package controller;

import dao.*;
import entity.*;
import i18n.I18nManager;
import view.SelectedCourseStudentsView;

import java.util.List;

/**
 * Controller responsible for managing students in a selected course view.
 *
 * <p>This class handles displaying students enrolled in a course and removing students
 * from the course. When a student is removed, related attendance records are also cleaned up.</p>
 *
 * <p>It acts as a bridge between {@link SelectedCourseStudentsView} and DAO layer.</p>
 */
public class SelectedCourseStudentsController {

    /** The ID of the selected course. */
    private final int courseId;

    /** DAO used for accessing course data from the database. */
    private final CourseDao courseDao = new CourseDao();

    /** View responsible for displaying course students. */
    private final SelectedCourseStudentsView view;

    /** DAO used for managing course enrollment (attends relationships). */
    private final AttendsDao attendsDao = new AttendsDao();


    /** The ID of the currently logged-in teacher. */
    private final int teacherId;

    /**
     * Constructs a new SelectedCourseStudentsController.
     *
     * @param courseView the view used to display course students
     * @param courseId the ID of the selected course
     */
    public SelectedCourseStudentsController(SelectedCourseStudentsView courseView, int courseId) {
        this.courseId = courseId;
        this.view = courseView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Updates the view title using the course identifier.
     */
    public void updateViewTitle() {
        Course course = courseDao.find(courseId);
        view.displayViewTitle(course.getIdentifier());
    }

    /**
     * Loads and displays all students enrolled in the selected course.
     */
    public void displayStudents() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        view.clearStudentsList();
        List<Attends> attends = attendsDao.findByCourse(courseId);
        for (Attends anAttends : attends) {
            Student student = anAttends.getStudent();
            view.addToStudentsList(student.getFirstname(lang), student.getLastname(lang), student.getId());
        }
    }

    /**
     * Removes a student from the course and deletes all related attendance records.
     *
     * <p>This ensures that the student's participation is fully removed from both
     * enrollment and attendance tracking data.</p>
     *
     * @param studentId the ID of the student to remove
     */
    public void removeStudentFromCourse(int studentId) {
        attendsDao.delete(courseId, studentId);

        AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        ChecksDao checksDao = new ChecksDao();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            checksDao.delete(attendanceCheck.getId(), studentId);
            }
        }
    }
