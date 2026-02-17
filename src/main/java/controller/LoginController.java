package controller;

import dao.TeacherDao;
import entity.Teacher;
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
    private Teacher loggedInTeacher;

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
     * @throws Exception "Teacher with given email was not found" - Wrong email or teacher does not exist
     * @throws Exception "Incorrect password" - Password does not match the password in the database
     */
    public void tryLogin() throws Exception {
            Teacher foundTeacher = teacherDao.findByEmail(view.getLoginEmailValue());

            if (foundTeacher == null) {
                throw new Exception("Teacher with given email was not found");
            }

            if (!Objects.equals(foundTeacher.getPassword(), view.getLoginPasswordValue())) {
                throw new Exception("Incorrect password");
            }
            loggedInTeacher = foundTeacher;
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
    public Teacher getLoggedInTeacher() {
        return loggedInTeacher;
    }

}
