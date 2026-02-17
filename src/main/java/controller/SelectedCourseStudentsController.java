package controller;

import dao.AttendsDao;
import dao.CourseDao;
import dao.StudentDao;
import entity.AttendanceCheck;
import entity.Attends;
import entity.Course;
import entity.Student;
import view.SelectedCourseStudentsView;

import java.util.List;

public class SelectedCourseStudentsController {

    private Course course;
    private CourseDao courseDao = new CourseDao();
    private SelectedCourseStudentsView view;
    private StudentDao studentDao = new StudentDao();
    private AttendsDao attendsDao = new AttendsDao();

    public SelectedCourseStudentsController(SelectedCourseStudentsView courseView, int courseId) {
        this.course = courseDao.find(courseId);
        this.view = courseView;
    }

    public void updateViewTitle() {
        view.displayViewTitle(course.getIdentifier());
    }

    public void displayStudents() {
        view.clearStudentsList();
        List<Attends> attends = attendsDao.findByCourse(course);
        for (Attends anAttends : attends) {
            Student student = anAttends.getStudent();
            view.addToStudentsList(student.getFirstname(), student.getLastname(), student.getId());
        }
    }
}
