package controller;

import dao.TeacherDao;
import entity.Teacher;
import i18n.I18nManager;
import utils.PasswordHasher;
import view.LoginView;

import java.util.Objects;

/**
 * LoginController class, for communication between a LoginView other controllers and dao and entity classes
 * @version 1.0
 */
public class LoginController {

    /** The static LoginController instance */
    private static LoginController instance;

    /** The instance of the LoginView class */
    private LoginView view;
    /** The TeacherDao class instance for database operations on the teacher table */
    private TeacherDao teacherDao = new TeacherDao();
    /** The logged in Teacher entity for teacher data */
    private int loggedInTeacherId = 0;

    /**
     * The private constructor for the LoginController class
     */
    private LoginController() {}

    /**
     * Static method for the creating once and getting the LoginController instance
     * @return the LoginController instance
     */
    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    /**
     * Method for teacher login by getting given email and password. Sets loggedInTeacher attribute if successful.
     */
    public void tryLogin() {
        try {
            Teacher foundTeacher = teacherDao.findByEmail(view.getLoginEmailValue());
            if (foundTeacher == null) {
                throw new Exception(I18nManager.getResourceBundle().getString("login.error.email"));
            }

            String hash = foundTeacher.getPassword();
            boolean isMatch = PasswordHasher.comparePasswords(view.getLoginPasswordValue(), hash);

            if (!isMatch) {
                throw new Exception(I18nManager.getResourceBundle().getString("login.error.password"));
            }
            loggedInTeacherId = foundTeacher.getId();
        } catch (Exception e) {
            e.printStackTrace();
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Method for teacher logout. Resets the loggedInTeacherId and instance attributes.
     */
    public void logout() {
        loggedInTeacherId = 0;
        instance = null;
    }

    /**
     * Sets the view attribute with LoginView class instance
     * @param loginView The instance of the LoginView class
     */
    public void setLoginView(LoginView loginView) {
        this.view = loginView;
    }

    /**
     * Gets the value of the loggedInTeacher attribute
     * @return the Teacher entity that is currently logged in
     */
    public int getLoggedInTeacherId() {
        return loggedInTeacherId;
    }

}
