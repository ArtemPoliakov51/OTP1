package controller;

import dao.CourseDao;
import dao.TeacherDao;
import entity.Course;
import entity.Teacher;
import i18n.I18nManager;
import view.AllCoursesView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * AllCoursesController class, for communication between a AllCoursesView and dao and entity classes
 * @version 1.0
 */
public class AllCoursesController {

    /** The AllCoursesView class instance */
    private AllCoursesView allCoursesView;
    /** The logged in Teacher entity for teacher data */
    private int teacherId;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();

    /**
     * Constructor for SelectedCourseStudentsController
     * @param view The instance of the AllCoursesView class
     */
    public AllCoursesController(AllCoursesView view) {
        this.allCoursesView = view;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Method for deciding which courses are passed for the view
     * @param isActiveCourses The boolean value to decide which courses are shown. False for archived courses and true for active courses.
     */
    public void displayCourses(boolean isActiveCourses) {
        if (!isActiveCourses) {
            displayArchivedCourses();
        } else {
            displayActiveCourses();
        }
    }

    /**
     * Method for finding all the teacher's active courses and their data and passing the data for the view
     */
    public void displayActiveCourses() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacherId);
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ACTIVE")) {
                allCoursesView.addToActiveCoursesList(course.getIdentifier(), course.getName(lang), course.getCreated(), course.getId());
            }
        }
    }

    /**
     * Method for finding all the teacher's archived courses and their data and passing the data for the view
     */
    public void displayArchivedCourses() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacherId);
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ARCHIVED")) {
                allCoursesView.addToArchivedCoursesList(course.getIdentifier(), course.getName(lang), course.getCreated(), course.getArchived(), course.getId());
            }
        }
    }

    public void showTeacherInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        allCoursesView.displayTeacherInfo(teacher.getFirstname(lang), teacher.getLastname(lang), teacher.getEmail());
    }

    public void archiveCourse(int courseId) {
        Course course = courseDao.find(courseId);
        if (course == null) return;

        course.setStatus("ARCHIVED");
        course.setArchived(LocalDateTime.now()); // if your entity has archived timestamp
        courseDao.update(course);

        displayActiveCourses();  // Refresh UI
    }

    public void activateCourse(int courseId) {
        Course course = courseDao.find(courseId);
        if (course == null) return;

        course.setStatus("ACTIVE");
        course.setArchived(null); // clear archived date if needed
        courseDao.update(course);

        displayArchivedCourses(); // Refresh UI
    }
}
