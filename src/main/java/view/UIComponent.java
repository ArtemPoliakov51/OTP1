package view;

import controller.LoginController;
import service.I18nManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates the top and side panels that the views need.
 * The displayed teacher info is also updated through this class.
 */
public class UIComponent {

    /**
     * The label for teacher's name.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private static final Label teacherLabel = new Label();

    /**
     * The label for teacher's email.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private static final Label teacherEmailLabel = new Label();

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(UIComponent.class.getName());

    /**
     * A private constructor for this UI component class
     */
    private UIComponent() {}

    /**
     * Creates the top panel for the views.
     *
     * @return the top panel as a VBox
     */
    public static VBox getTopBar() {
        VBox topBar = new VBox();
        topBar.getStyleClass().add("appTitleBar");
        Label topBarLabel = new Label("ATTENDANCE CHECKER");
        topBarLabel.getStyleClass().add("appTitleBarTitle");
        topBar.getChildren().add(topBarLabel);
        return topBar;
    }

    /**
     * Creates the left side panel for the views.
     * @param stage the current app window
     * @param view the view the panel is for
     * @return the left side panel as a VBox
     */
    public static AnchorPane getLeftSideBar(Stage stage, UIView view) {
        VBox leftSideBarTop = new VBox();
        leftSideBarTop.getStyleClass().add("leftSideBarTop");
        VBox leftSideBarBottom = new VBox();
        leftSideBarBottom.getStyleClass().add("leftSideBarBottom");

        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);

        Button homeButton = new Button(I18nManager.getResourceBundle().getString("general.button.home"));
        homeButton.getStyleClass().add("homeButton");
        homeButton.setOnAction(actionEvent -> {
            try {
                // Move back to the Home view (AllCoursesView)
                AllCoursesView allCoursesView = new AllCoursesView(stage);
                allCoursesView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to return to home view.", e);
            }
        });

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.getStyleClass().add("languageButton");

        languageButton.setOnAction(actionEvent -> {
            try {
                LanguageSelectorView.openLanguageSelectionWindow();
                //Reload view when window is closed
                view.openView();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to open the language selection modal.", e);
            }
        });

        Button logoutButton = new Button(I18nManager.getResourceBundle().getString("general.button.logout"));
        logoutButton.getStyleClass().add("logoutButton");
        logoutButton.setOnAction(actionEvent -> {
            LoginController.logout();
            LoginView loginView = new LoginView();
            loginView.openLoginView(stage);
        });

        leftSideBarBottom.getChildren().addAll(homeButton, languageButton, logoutButton);

        AnchorPane leftSideBar = new AnchorPane();
        leftSideBar.getStyleClass().add("leftSideBar");
        leftSideBar.getChildren().addAll(leftSideBarTop, leftSideBarBottom);
        AnchorPane.setTopAnchor(leftSideBarTop, 20.0);
        AnchorPane.setBottomAnchor(leftSideBarBottom, 20.0);

        return leftSideBar;
    }

    /**
     * Displays the teacher's information in the sidebar.
     *
     * @param firstname the teacher's firstname
     * @param lastname the teacher's lastname
     * @param email the teacher's email address
     */
    public static void displayTeacherInfo(String firstname, String lastname, String email) {
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        teacherLabel.setText(firstname.toUpperCase() + separator + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
    }
}
