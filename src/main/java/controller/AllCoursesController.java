package controller;

import dao.CourseDao;
import entity.Course;
import service.I18nManager;
import view.AllCoursesView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Controller responsible for managing the interaction between {@link AllCoursesView}
 * and the data layer for course-related operations.
 *
 * <p>This controller handles retrieving and displaying both active and archived courses
 * belonging to the currently logged-in teacher. It also supports course state changes
 * such as archiving and reactivating courses.</p>
 *
 * <p>Additionally, it provides functionality for displaying teacher information in the view.</p>
 */
public class AllCoursesController {

    /** The view responsible for displaying all courses. */
    private final AllCoursesView allCoursesView;

    /** The ID of the currently logged-in teacher. */
    private final int teacherId;

    /** DAO used for accessing and managing course data in the database. */
    private final CourseDao courseDao = new CourseDao();

    /**
     * Constructs a new AllCoursesController.
     *
     * @param view the view used to display active and archived courses
     */
    public AllCoursesController(AllCoursesView view) {
        this.allCoursesView = view;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Determines which courses to display in the view based on their status.
     *
     * @param isActiveCourses if true, active courses are displayed;
     *                        if false, archived courses are displayed
     */
    public void displayCourses(boolean isActiveCourses) {
        if (!isActiveCourses) {
            displayArchivedCourses();
        } else {
            displayActiveCourses();
        }
    }

    /**
     * Retrieves and displays all active courses belonging to the logged-in teacher.
     * Clears the current view before adding updated course data.
     */
    public void displayActiveCourses() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacherId);

        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ACTIVE")) {
                allCoursesView.addToActiveCoursesList(
                        course.getIdentifier(),
                        course.getName(lang),
                        course.getCreated(),
                        course.getId()
                );
            }
        }
    }

    /**
     * Retrieves and displays all archived courses belonging to the logged-in teacher.
     * Clears the current view before adding updated course data.
     */
    public void displayArchivedCourses() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacherId);

        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ARCHIVED")) {
                allCoursesView.addToArchivedCoursesList(
                        course.getIdentifier(),
                        course.getName(lang),
                        course.getCreated(),
                        course.getArchived(),
                        course.getId()
                );
            }
        }
    }

    /**
     * Archives a course by setting its status to ARCHIVED and storing the archive timestamp.
     * After updating, the active course list is refreshed in the view.
     *
     * @param courseId the ID of the course to archive
     */
    public void archiveCourse(int courseId) {
        Course course = courseDao.find(courseId);
        if (course == null) return;

        course.setStatus("ARCHIVED");
        course.setArchived(LocalDateTime.now());
        courseDao.update(course);

        displayActiveCourses();
    }

    /**
     * Reactivates an archived course by setting its status to ACTIVE
     * and removing the archive timestamp.
     * After updating, the archived course list is refreshed in the view.
     *
     * @param courseId the ID of the course to activate
     */
    public void activateCourse(int courseId) {
        Course course = courseDao.find(courseId);
        if (course == null) return;

        course.setStatus("ACTIVE");
        course.setArchived(null);
        courseDao.update(course);

        displayArchivedCourses();
    }
}
