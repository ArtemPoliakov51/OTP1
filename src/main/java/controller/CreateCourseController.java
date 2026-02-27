package controller;

import dao.CourseDao;
import dao.TeacherDao;
import entity.Teacher;
import view.CreateCourseView;

public class CreateCourseController {

    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    /** The CreateCourseView class instance */
    private CreateCourseView view;

    private int teacherId;

    /**
     * Constructor for CreateCourseController
     * @param createCourseView The instance of the SelectedCourseStudentsView class
     */
    public CreateCourseController(CreateCourseView createCourseView) {
        this.view = createCourseView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }


    public void showTeacherInfo() {
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        view.displayTeacherInfo(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail());
    }

}
