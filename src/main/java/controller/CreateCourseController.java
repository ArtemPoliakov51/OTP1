package controller;

import dao.CourseDao;
import dao.TeacherDao;
import entity.Teacher;
import i18n.I18nManager;
import view.CreateCourseView;

/**
 * Controller responsible for handling course creation logic and communication
 * between {@link CreateCourseView} and the data layer.
 *
 * <p>This class provides functionality for retrieving and displaying teacher information
 * and (when enabled) creating new courses for the logged-in teacher.</p>
 */
public class CreateCourseController {

    /** DAO used for accessing and managing course data in the database. */
    private final CourseDao courseDao = new CourseDao();

    /** The view responsible for creating a new course. */
    private final CreateCourseView view;

    /** The ID of the currently logged-in teacher. */
    private final int teacherId;

    /**
     * Constructs a new CreateCourseController.
     *
     * @param createCourseView the view used for creating courses
     */
    public CreateCourseController(CreateCourseView createCourseView) {
        this.view = createCourseView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Retrieves and displays information about the currently logged-in teacher.
     * The data is localized based on the current language setting.
     */
    public void showTeacherInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        view.displayTeacherInfo(teacher.getFirstname(lang), teacher.getLastname(lang), teacher.getEmail());
    }

    // Disabled for now:
    /**
     * Creates a new course for the logged-in teacher.
     *
     * <p>This method validates input values and persists a new course to the database.</p>
     *
     * @param name the name of the course
     * @param identifier the unique course identifier
     * @return true if the course was successfully created, otherwise false
     */
    /*
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
     */
}
