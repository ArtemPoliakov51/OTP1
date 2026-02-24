package controller;

import dao.CourseDao;
import view.CreateCourseView;

public class CreateCourseController {

    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The CreateCourseView class instance */
    private CreateCourseView view;

    /**
     * Constructor for CreateCourseController
     * @param createCourseView The instance of the SelectedCourseStudentsView class
     */
    public CreateCourseController(CreateCourseView createCourseView) {
        this.view = createCourseView;
    }

}
