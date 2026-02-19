package controller;

import dao.*;
import entity.*;
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

    private List<Student> studentsNotInCourse = new ArrayList<>();

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

        List<Student> allStudents = studentDao.findAll();
        for (Student student : allStudents) {
            if (!studentsInCourse.contains(student)) {
                studentsNotInCourse.add(student);
            }
        }

        for (Student student : studentsNotInCourse) {
            view.addToStudentList(student.getId(), student.getFirstname(), student.getLastname());
        }
    }

    public void filterStudents(String key) {
        view.clearStudentsList();

        for (Student student : studentsNotInCourse) {
            if (student.getFirstname().toLowerCase().contains(key.toLowerCase()) || student.getLastname().toLowerCase().contains(key.toLowerCase())){
                view.addToStudentList(student.getId(), student.getFirstname(), student.getLastname());
            }
        }
    }

    public void addStudentsToCourse(List<Integer> studentIds) {
        AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);
        ChecksDao checksDao = new ChecksDao();

        for (Integer id : studentIds) {
            Student found = studentDao.find(id);
            Attends attends = new Attends(course, found);
            attendsDao.persist(attends);
            for (AttendanceCheck attendanceCheck : attendanceChecks) {
                Checks checks = new Checks(found, attendanceCheck);
                checksDao.persist(checks);
            }
        }
    }
}
