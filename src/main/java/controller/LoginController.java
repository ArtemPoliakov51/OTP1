package controller;

import dao.TeacherDao;
import entity.Teacher;
import i18n.I18nManager;
import utils.PasswordHasher;
import view.LoginView;
import view.UIComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller responsible for handling authentication logic in the application.
 *
 * <p>This class manages teacher login and logout functionality and acts as a central
 * access point for retrieving the currently logged-in teacher.</p>
 *
 * <p>It communicates with {@link LoginView}, {@link TeacherDao}, and authentication utilities
 * such as {@link PasswordHasher}.</p>
 *
 * <p>This controller is implemented as a singleton.</p>
 */
public class LoginController {

    /** Singleton instance of LoginController. */
    private static LoginController instance;

    /** The view responsible for login UI interactions. */
    private static LoginView view;

    /** DAO used for accessing teacher data from the database. */
    private static final TeacherDao teacherDao = new TeacherDao();

    /** The ID of the currently logged-in teacher (0 if no user is logged in). */
    private static int loggedInTeacherId = 0;

    /**
     * Logger used for recording authentication-related events, warnings,
     * and unexpected errors occurring within the LoginController.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(LoginController.class.getName());

    /**
     * Private constructor to enforce singleton pattern.
     */
    private LoginController() {}

    /**
     * Returns the singleton instance of LoginController.
     *
     * @return the shared LoginController instance
     */
    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }

    /**
     * Attempts to authenticate a teacher using credentials provided by the view.
     *
     * <p>If authentication is successful, the teacher ID is stored as the logged-in user.
     * Otherwise, an error message is displayed in the view.</p>
     */
    public static void tryLogin() {
        try {
            Teacher foundTeacher = teacherDao.findByEmail(view.getLoginEmailValue());
            if (foundTeacher == null) {
                throw new IllegalArgumentException(I18nManager.getResourceBundle().getString("login.error.email"));
            }

            String hash = foundTeacher.getPassword();
            boolean isMatch = PasswordHasher.comparePasswords(view.getLoginPasswordValue(), hash);

            if (!isMatch) {
                throw new IllegalArgumentException(I18nManager.getResourceBundle().getString("login.error.password"));
            }
            loggedInTeacherId = foundTeacher.getId();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Login attempt failed", e);
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Logs out the current user and resets the controller state.
     *
     * <p>This clears the stored teacher ID and resets the singleton instance.</p>
     */
    public static void logout() {
        loggedInTeacherId = 0;
        instance = null;
    }

    /**
     * Sets the login view instance used by this controller.
     *
     * @param loginView the LoginView instance
     */
    public static void setLoginView(LoginView loginView) {
        view = loginView;
    }

    /**
     * Returns the ID of the currently logged-in teacher.
     *
     * @return teacher ID, or 0 if no teacher is logged in
     */
    public int getLoggedInTeacherId() {
        return loggedInTeacherId;
    }

    /**
     * Retrieves and displays information about the currently logged-in teacher.
     * The data is passed to the view for presentation.
     */
    public void showTeacherInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Teacher teacher = teacherDao.find(loggedInTeacherId);

        UIComponent.displayTeacherInfo(
                teacher.getFirstname(lang),
                teacher.getLastname(lang),
                teacher.getEmail()
        );
    }

}
