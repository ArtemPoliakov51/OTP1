package controller;

import dao.AttendsDao;
import dao.CourseDao;
import dao.StudentDao;
import entity.Attends;
import entity.Course;
import entity.Student;
import view.AddStudentsView;

import java.util.ArrayList;
import java.util.List;

public class AddStudentsController {

    /** The Course entity for course data */
    private Course course;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The AddStudentsView class instance */
    private AddStudentsView view;
    private AttendsDao attendsDao = new AttendsDao();
    private StudentDao studentDao = new StudentDao();

    /**
     * Constructor for AddStudentsController
     * @param courseView The instance of the AddStudentsView class
     * @param courseId The unique ID of the course
     */
    public AddStudentsController(AddStudentsView courseView, int courseId) {
        this.course = courseDao.find(courseId);
        this.view = courseView;
    }

    /**
     * Method for passing the course's unique identifier for the view
     */
    public void updateViewTitle() {
        view.displayViewTitle(course.getIdentifier());
    }

    public void displayAvailableStudents() {
        List<Attends> attends = attendsDao.findByCourse(course);
        List<Student> studentsInCourse = new ArrayList<>();
        for (Attends anAttends : attends) {
            studentsInCourse.add(anAttends.getStudent());
        }

        List<Student> studentsNotInCourse = new ArrayList<>();

        List<Student> allStudents = studentDao.findAll();
        for (Student student : allStudents) {
            if (!studentsInCourse.contains(student)) {
                studentsNotInCourse.add(student);
            }
        }

        for (Student aStudent : studentsNotInCourse) {
            view.addToStudentList(aStudent.getId(), aStudent.getFirstname(), aStudent.getLastname());
        }
    }
}
