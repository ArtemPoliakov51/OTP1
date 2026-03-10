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

    public boolean createACourse(String name, String identifier) {
        if (name == null || name.isBlank() || identifier == null || identifier.isBlank()) {
            return false;
        }

        // Use the existing teacherId already stored in controller
        try {
            int newId = courseDao.persist(name, identifier.toUpperCase(), teacherId);
            return newId > 0;   // successfully created
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
