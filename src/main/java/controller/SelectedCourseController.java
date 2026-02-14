package controller;

import dao.CourseDao;
import entity.Course;
import javafx.scene.layout.*;
import view.SelectedCourseView;


public class SelectedCourseController {

    private Course course;
    private CourseDao courseDao = new CourseDao();
    private SelectedCourseView courseView;

    public SelectedCourseController(SelectedCourseView courseView, int courseId) {
        this.course = courseDao.find(courseId);
        this.courseView = courseView;
    }

    public void updateViewTitle() {
        courseView.displayViewTitle(course.getIdentifier());
    }

    public void updateCourseInfo() {
        courseView.displayCourseNameAndIdentifier(course.getName(), course.getIdentifier());
    }
}
