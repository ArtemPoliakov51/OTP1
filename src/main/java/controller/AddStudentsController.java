package controller;

import dao.*;
import entity.*;
import view.AddStudentsView;

import java.util.ArrayList;
import java.util.List;

public class AddStudentsController {

    /** The Course entity for course data */
    private int courseId;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The AddStudentsView class instance */
    private AddStudentsView view;
    private AttendsDao attendsDao = new AttendsDao();
    private StudentDao studentDao = new StudentDao();

    private int teacherId;

    private List<Student> studentsNotInCourse = new ArrayList<>();

    /**
     * Constructor for AddStudentsController
     * @param courseView The instance of the AddStudentsView class
     * @param courseId The unique ID of the course
     */
    public AddStudentsController(AddStudentsView courseView, int courseId) {
        this.courseId = courseId;
        this.view = courseView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Method for passing the course's unique identifier for the view
     */
    public void updateViewTitle() {
        Course course = courseDao.find(courseId);
        view.displayViewTitle(course.getIdentifier());
    }

    public void displayAvailableStudents() {
        List<Attends> attends = attendsDao.findByCourse(courseId);

        List<Integer> studentsInCourse = new ArrayList<>();
        for (Attends anAttends : attends) {
            System.out.println(anAttends.getStudent().getFirstname());
            studentsInCourse.add(anAttends.getStudent().getId());
        }

        List<Student> allStudents = studentDao.findAll();
        for (Student student : allStudents) {
            System.out.println("All: " + student.getFirstname());
            if (!studentsInCourse.contains(student.getId())) {
                System.out.println("Not in course: " + student.getFirstname());
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
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        ChecksDao checksDao = new ChecksDao();

        for (Integer id : studentIds) {
            Student found = studentDao.find(id);
            attendsDao.persist(courseId, found.getId());
            for (AttendanceCheck attendanceCheck : attendanceChecks) {
                checksDao.persist(attendanceCheck.getId(), found.getId());
            }
        }
    }

    public void showTeacherInfo() {
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        view.displayTeacherInfo(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail());
    }
}
