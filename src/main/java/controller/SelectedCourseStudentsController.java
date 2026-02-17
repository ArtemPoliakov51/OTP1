package controller;

import dao.AttendsDao;
import dao.CourseDao;
import entity.Attends;
import entity.Course;
import entity.Student;
import view.SelectedCourseStudentsView;

import java.util.List;

/**
 * SelectedCourseStudentsController class, for communication between a SelectedCourseStudentsView and dao and entity classes
 * @version 1.0
 */
public class SelectedCourseStudentsController {

    /** The Course entity for course data */
    private Course course;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The SelectedCourseStudentsView class instance */
    private SelectedCourseStudentsView view;
    /** The CourseDao class instance for database operations on the attends table */
    private AttendsDao attendsDao = new AttendsDao();

    /**
     * Constructor for SelectedCourseStudentsController
     * @param courseView The instance of the SelectedCourseStudentsView class
     * @param courseId The unique ID of the course
     */
    public SelectedCourseStudentsController(SelectedCourseStudentsView courseView, int courseId) {
        this.course = courseDao.find(courseId);
        this.view = courseView;
    }

    /**
     * Method for passing the course's unique identifier for the view
     */
    public void updateViewTitle() {
        view.displayViewTitle(course.getIdentifier());
    }

    /**
     * Method for finding and passing the course's students' info for the view
     */
    public void displayStudents() {
        view.clearStudentsList();
        List<Attends> attends = attendsDao.findByCourse(course);
        for (Attends anAttends : attends) {
            Student student = anAttends.getStudent();
            view.addToStudentsList(student.getFirstname(), student.getLastname(), student.getId());
        }
    }
}
