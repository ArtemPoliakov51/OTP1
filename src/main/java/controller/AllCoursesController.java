package controller;

import dao.CourseDao;
import entity.Course;
import view.AllCoursesView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllCoursesController {

    private AllCoursesView allCoursesView;
    private LoginController loginController;
    private CourseDao courseDao = new CourseDao();

    public AllCoursesController(AllCoursesView view, LoginController loginController) {
        this.allCoursesView = view;
        this.loginController = loginController;
    }

    public void displayActiveCourses() {
        List<Course> allCourses = courseDao.findByTeacher(loginController.getLoggedInTeacher());
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ACTIVE")) {
                allCoursesView.addToActiveCoursesList(course.getIdentifier(), course.getName(), course.getCreated(), course.getId());
            }
        }
    }
}
