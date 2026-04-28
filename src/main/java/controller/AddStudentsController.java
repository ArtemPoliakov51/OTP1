package controller;

import dao.*;
import entity.*;
import service.I18nManager;
import view.AddStudentsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the interaction between {@link AddStudentsView}
 * and the data layer for student-related operations.
 *
 * <p>This class handles retrieving students who are not yet enrolled in a course,
 * filtering them based on user input, and adding selected students to the course.
 * It also initializes related attendance check data when students are added.</p>
 *
 * <p>Additionally, the controller provides functionality for updating the view title
 * with course information and displaying the logged-in teacher's details.</p>
 */
public class AddStudentsController {

    /** The ID of the course being modified. */
    private final int courseId;

    /** DAO for accessing course data from the database. */
    private final CourseDao courseDao = new CourseDao();

    /** Reference to the view responsible for adding students. */
    private final AddStudentsView view;

    /** DAO for managing course attendance relationships. */
    private final AttendsDao attendsDao = new AttendsDao();

    /** DAO for accessing student data. */
    private final StudentDao studentDao = new StudentDao();

    /** List of students who are not yet enrolled in the course. */
    private final List<Student> studentsNotInCourse = new ArrayList<>();

    /**
     * Constructs a new AddStudentsController.
     *
     * @param courseView the view used for displaying and managing student additions
     * @param courseId the unique identifier of the course being modified
     */
    public AddStudentsController(AddStudentsView courseView, int courseId) {
        this.courseId = courseId;
        this.view = courseView;
    }

    /**
     * Updates the view title using the course identifier.
     * Retrieves the course from the database and displays its identifier in the view.
     */
    public void updateViewTitle() {
        Course course = courseDao.find(courseId);
        view.displayViewTitle(course.getIdentifier());
    }

    /**
     * Loads and displays all students who are not currently enrolled in the course.
     * Filters out students already assigned to the course and sends the remaining
     * students to the view for display.
     */
    public void displayAvailableStudents() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        List<Attends> attends = attendsDao.findByCourse(courseId);

        List<Integer> studentsInCourse = new ArrayList<>();
        for (Attends anAttends : attends) {
            studentsInCourse.add(anAttends.getStudent().getId());
        }

        List<Student> allStudents = studentDao.findAll();
        for (Student student : allStudents) {
            if (!studentsInCourse.contains(student.getId())) {
                studentsNotInCourse.add(student);
            }
        }

        for (Student student : studentsNotInCourse) {
            view.addToStudentList(student.getId(), student.getFirstname(lang), student.getLastname(lang));
        }
    }

    /**
     * Filters the list of available students based on a search keyword.
     * The search is performed on both firstname and lastname (case-insensitive).
     *
     * @param key the search term used to filter students
     */
    public void filterStudents(String key) {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        view.clearStudentsList();

        for (Student student : studentsNotInCourse) {
            if (student.getFirstname(lang).toLowerCase().contains(key.toLowerCase()) || student.getLastname(lang).toLowerCase().contains(key.toLowerCase())) {
                view.addToStudentList(student.getId(), student.getFirstname(lang), student.getLastname(lang));
            }
        }
    }

    /**
     * Adds selected students to the course.
     * Also initializes attendance check records for each student
     * based on existing course attendance checks.
     *
     * @param studentIds list of student IDs to be added to the course
     */
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
}
