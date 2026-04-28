package view;

import controller.LoginController;
import controller.SelectedCourseStudentsController;
import service.I18nManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX view for displaying all students in a selected course.
 * <p>
 * This view shows course-specific student information, allows navigation
 * to student reports, and provides functionality for adding and removing
 * students from the course.
 * </p>
 *
 * <p>
 * The view communicates with {@link SelectedCourseStudentsController}
 * for loading and modifying student data.
 * </p>
 */
public class SelectedCourseStudentsView implements UIView {

    /**
     * The primary stage or window of the application.
     */
    private final Stage primaryStage;

    /**
     * The controller for this view.
     */
    private final SelectedCourseStudentsController courseStudentsController;

    /**
     * The selected course ID.
     */
    private final int courseId;

    /**
     * The title label for the view.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label viewTitle = new Label();

    /**
     * The VBox list of students that are currently enrolled to the course.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final VBox studentsList = new VBox();

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(SelectedCourseStudentsView.class.getName());

    /**
     * Constructs the course students view for a specific course.
     *
     * @param primaryStage main application stage
     * @param courseId ID of the course whose students are displayed
     */
    protected SelectedCourseStudentsView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.courseStudentsController = new SelectedCourseStudentsController(this, courseId);
        this.courseId = courseId;
    }

    /**
     * Builds and displays the view displaying all the students enrolled on the course.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * list of students and action buttons for removing and adding students to the course.</p>
     *
     */
    public void openView() {
        BorderPane viewBasicLayout = new BorderPane();

        // The common layout for all the view (other than the login):
        VBox topBar = UIComponent.getTopBar();
        LoginController.getInstance().showTeacherInfo();
        AnchorPane leftSideBar = UIComponent.getLeftSideBar(primaryStage, this);

        // CENTER CONTENT:
        BorderPane center = new BorderPane();
        center.getStyleClass().add("center");

        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("titleBar");
        viewTitle = new Label();
        viewTitle.getStyleClass().add("viewTitle");
        titleBar.getChildren().add(viewTitle);
        courseStudentsController.updateViewTitle();

        center.setTop(titleBar);

        // HBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox goBackBtnBox = new HBox();
        goBackBtnBox.getStyleClass().add("goBackBtnBox");
        Button goBackButton = new Button(I18nManager.getResourceBundle().getString("general.button.goback"));
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(actionEvent -> {
            try {
                SelectedCourseView selectedCourseView = new SelectedCourseView(primaryStage, courseId);
                selectedCourseView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to go back a page.", e);
            }
        });

        goBackBtnBox.getChildren().add(goBackButton);

        // VBox for the course students:
        VBox courseStudentsBox = new VBox(20);
        courseStudentsBox.getStyleClass().add("courseStudentsBox");
        HBox.setHgrow(courseStudentsBox, Priority.ALWAYS);
        courseStudentsBox.setMaxWidth(Double.MAX_VALUE);

        Label courseStudentsLabel = new Label(I18nManager.getResourceBundle().getString("students.title"));
        courseStudentsLabel.getStyleClass().add("courseStudentsLabel");

        studentsList.getStyleClass().add("studentsList");
        studentsList.setSpacing(8);
        ScrollPane studentListBox = new ScrollPane(studentsList);
        studentListBox.getStyleClass().add("studentListBox");
        courseStudentsController.displayStudents();

        Button addStudentsButton = new Button(I18nManager.getResourceBundle().getString("students.button.add"));
        addStudentsButton.getStyleClass().add("addStudentsButton");

        addStudentsButton.setOnAction(actionEvent -> {
            try {
                AddStudentsView addStudentsView = new AddStudentsView(primaryStage, courseId);
                addStudentsView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to open the view for adding student.", e);
            }
        });

        courseStudentsBox.getChildren().addAll(courseStudentsLabel, studentListBox, addStudentsButton);

        content.getChildren().addAll(goBackBtnBox, courseStudentsBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/selectedcourse_students_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.students"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    /**
     * Adds a student entry to the UI list.
     *
     * @param firstname student's firstname
     * @param lastname student's lastname
     * @param studentId student ID
     */
    public void addToStudentsList(String firstname, String lastname, int studentId) {
        HBox studentInsert = new HBox();
        studentInsert.getStyleClass().add("studentItem");

        HBox studentInfo = new HBox();
        studentInfo.getStyleClass().add("studentInfo");

        HBox studentName = new HBox(5);
        studentName.getStyleClass().add("studentName");
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        Label nameLabel = new Label(firstname + separator + lastname);
        studentName.getChildren().addAll(nameLabel);

        Button generateStudentReportBtn = new Button(I18nManager.getResourceBundle().getString("students.button.report").toUpperCase());
        generateStudentReportBtn.getStyleClass().add("studentReportButton");

        generateStudentReportBtn.setOnAction(actionEvent -> {
            try {
                StudentAttendanceReportView reportView = new StudentAttendanceReportView(primaryStage, courseId, studentId);
                reportView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to open the student report view.", e);
            }
        });

        studentInfo.getChildren().addAll(studentName, generateStudentReportBtn);
        HBox.setHgrow(studentName, Priority.ALWAYS);
        studentName.setMaxWidth(Double.MAX_VALUE);

        Button removeStudentButton = new Button("X");
        removeStudentButton.getStyleClass().add("removeStudentButton");

        removeStudentButton.setOnAction(actionEvent -> {
            try {
                courseStudentsController.removeStudentFromCourse(studentId);
                courseStudentsController.displayStudents();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to remove a student from the course.", e);
            }
        });

        studentInsert.getChildren().addAll(studentInfo, removeStudentButton);
        HBox.setHgrow(studentInfo, Priority.ALWAYS);
        studentInfo.setMaxWidth(Double.MAX_VALUE);

        studentsList.getChildren().add(studentInsert);
    }

    /**
     * Displays the view's title on the top of the main content.
     *
     * @param title the title of the view
     */
    public void displayViewTitle(String title) {
        viewTitle.setText(title);
    }

    /**
     * Clears all student entries from the UI list. Used when updating the view after removing a student.
     */
    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
