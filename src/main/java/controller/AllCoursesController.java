package controller;

import dao.CourseDao;
import dao.TeacherDao;
import entity.Course;
import entity.Teacher;
import view.AllCoursesView;

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
     * @param show The string value to decide which courses are shown. "ARCHIVED" for archived courses and "ACTIVE" for active courses.
     */
    public void displayCourses(String show) {
        if (show.equals("ARCHIVED")) {
            displayArchivedCourses();
        } else {
            displayActiveCourses();
        }
    }

    /**
     * Method for finding all the teacher's active courses and their data and passing the data for the view
     */
    public void displayActiveCourses() {
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacherId);
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ACTIVE")) {
                allCoursesView.addToActiveCoursesList(course.getIdentifier(), course.getName(), course.getCreated(), course.getId());
            }
        }
    }

    /**
     * Method for finding all the teacher's archived courses and their data and passing the data for the view
     */
    public void displayArchivedCourses() {
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacherId);
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ARCHIVED")) {
                allCoursesView.addToArchivedCoursesList(course.getIdentifier(), course.getName(), course.getCreated(), course.getArchived(), course.getId());
            }
        }
    }

    public void showTeacherInfo() {
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        allCoursesView.displayTeacherInfo(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail());
    }

    /**
     * Method fo activating archived course. Changes the course's status to "ACTIVE".
     * @param courseId The unique ID of the course
     */
    public void activateCourse(int courseId) {
        Course course = courseDao.find(courseId);
        course.setStatus("ACTIVE");
        course.setArchived(null);
        courseDao.update(course);
    }
}
