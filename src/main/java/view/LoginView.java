package view;

import controller.LoginController;
import service.I18nManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX view class for the login screen of the application.
 * <p>
 * This class initializes and displays the login UI, handles user input
 * for email and password, and delegates authentication logic to
 * {@link LoginController}.
 * </p>
 *
 * <p>
 * It also provides access to user input values and displays error messages
 * returned from the controller.
 * </p>
 */
public class LoginView extends Application {

    /**
     * The email field for user input.
     *
     * <p>Added as an attribute so it can be interacted with from different methods.</p>
     */
    private final TextField loginEmailField = new TextField();

    /**
     * The password field for user input.
     *
     * <p>Added as an attribute so it can be interacted with from different methods.</p>
     */
    private final PasswordField loginPasswordField = new PasswordField();

    /**
     * The label for error messages.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label errorMessage = new Label();

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(LoginView.class.getName());

    /**
     * Application entry point for JavaFX.
     * Sets the default locale and opens the login view.
     *
     * @param stage primary application window
     * @throws Exception if JavaFX initialization fails
     */
    @Override
    public void start(Stage stage) throws Exception {
        I18nManager.setLocale(new Locale("en", "US"));
        openLoginView(stage);
    }

    /**
     * Builds and displays the login view UI.
     *
     * @param stage the primary stage where the login scene is displayed
     */
    public void openLoginView(Stage stage) {
        LoginController.setLoginView(this);

        BorderPane loginLayout = new BorderPane();
        VBox titleBox = new VBox();
        VBox loginBox = new VBox();

        titleBox.setId("loginTitleBox");
        loginBox.setId("loginBox");

        Label loginTitle = new Label("ATTENDANCE CHECKER");
        loginTitle.setId("loginTitle");
        titleBox.getChildren().add(loginTitle);

        Label loginLabel = new Label(I18nManager.getResourceBundle().getString("login.title").toUpperCase());
        Label loginEmailLabel = new Label(I18nManager.getResourceBundle().getString("login.email.label"));
        Label loginPasswordLabel = new Label(I18nManager.getResourceBundle().getString("login.password.label"));

        loginLabel.setId("loginLabel");
        loginEmailLabel.getStyleClass().add("loginFieldLabel");
        loginPasswordLabel.getStyleClass().add("loginFieldLabel");

        loginEmailField.setPromptText(I18nManager.getResourceBundle().getString("login.email.prompt"));
        loginPasswordField.setPromptText(I18nManager.getResourceBundle().getString("login.password.prompt"));

        loginEmailField.getStyleClass().add("loginTextField");
        loginPasswordField.getStyleClass().add("loginTextField");

        Button loginButton = new Button(I18nManager.getResourceBundle().getString("login.button.main"));
        loginButton.setId("loginButton");

        loginButton.setOnAction(actionEvent -> {
            try {
                LoginController.tryLogin();
                AllCoursesView allCoursesView = new AllCoursesView(stage);
                allCoursesView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to login.", e);
            }
        });

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.setId("loginLanguageButton");

        languageButton.setOnAction(actionEvent -> {
            try {
                LanguageSelectorView.openLanguageSelectionWindow();
                //Reload view when window is closed
                openLoginView(stage);
                errorMessage.setText("");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to change the language.", e);
            }
        });

        errorMessage.getStyleClass().add("error");

        loginBox.getChildren().addAll(loginLabel, loginEmailLabel, loginEmailField,
                loginPasswordLabel, loginPasswordField, loginButton, errorMessage, languageButton);


        VBox.setMargin(loginEmailField, new Insets(5, 0, 20, 0));
        VBox.setMargin(loginPasswordField, new Insets(5, 0, 20, 0));
        VBox.setMargin(loginButton, new Insets(15, 0, 0, 0));

        loginLayout.setTop(titleBox);
        loginLayout.setCenter(loginBox);

        Scene scene = new Scene(loginLayout, 850, 500);
        scene.getStylesheets().add("/styles/login_style.css");
        stage.setTitle(I18nManager.getResourceBundle().getString("window.login"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Reads the email field and returns its value. Used for getting user input for login.
     * @return the user input teacher email
     */
    public String getLoginEmailValue() {
        return loginEmailField.getText();
    }

    /**
     * Reads the password field and returns its value. Used for getting user input for login.
     * @return the user input teacher password
     */
    public String getLoginPasswordValue() {
        return loginPasswordField.getText();
    }

    /**
     * Displays an error message on the UI if login attempt fails.
     * @param error the error message to be displayed
     */
    public void displayErrorMessage(String error) {
        errorMessage.setText(error);
    }
}
