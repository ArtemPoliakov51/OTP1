package controller;

import dao.CourseDao;
import entity.Course;
import entity.Teacher;
import view.AllCoursesView;

import java.util.List;
import java.util.Objects;

public class AllCoursesController {

    private AllCoursesView allCoursesView;
    private Teacher teacher;
    private CourseDao courseDao = new CourseDao();

    public AllCoursesController(AllCoursesView view, Teacher teacher) {
        this.allCoursesView = view;
        this.teacher = teacher;
    }

    public void displayCourses(String show) {
        if (show.equals("ARCHIVED")) {
            displayArchivedCourses();
        } else {
            displayActiveCourses();
        }
    }

    public void displayActiveCourses() {
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacher);
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ACTIVE")) {
                allCoursesView.addToActiveCoursesList(course.getIdentifier(), course.getName(), course.getCreated(), course.getId());
            }
        }
    }

    public void displayArchivedCourses() {
        allCoursesView.clearCoursesList();
        List<Course> allCourses = courseDao.findByTeacher(teacher);
        for (Course course : allCourses) {
            if (Objects.equals(course.getStatus(), "ARCHIVED")) {
                allCoursesView.addToArchivedCoursesList(course.getIdentifier(), course.getName(), course.getCreated(), course.getArchived(), course.getId());
            }
        }
    }
}
