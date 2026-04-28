package view;

import controller.LoginController;
import controller.SelectedCourseController;
import service.I18nManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX view for displaying a selected course.
 * <p>
 * This view shows course details such as name, identifier, attendance percentage,
 * and a list of attendance checks. It also provides navigation to student lists,
 * reports, and individual attendance check views.
 * </p>
 *
 * <p>
 * The view communicates with {@link SelectedCourseController} to load and manage data.
 * </p>
 */
public class SelectedCourseView implements UIView {

    /**
     * The primary stage or window of the application.
     */
    private final Stage primaryStage;

    /**
     * The controller for this view.
     */
    private final SelectedCourseController courseController;

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
     * The label for course's name.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label courseNameLabel = new Label();

    /**
     * The label for course's identifier.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label courseIdentLabel = new Label();

    /**
     * The label for course's attendance percentage.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label courseAttendPercentage = new Label();

    /**
     * The VBox list of attendance checks that belong to the course.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final VBox attendanceChecksList = new VBox();

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(SelectedCourseView.class.getName());


    /**
     * Constructs a view for a specific course.
     *
     * @param primaryStage main application stage
     * @param courseId ID of the course to display
     */
    protected SelectedCourseView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.courseController = new SelectedCourseController(this, courseId);
        this.courseId = courseId;
    }

    /**
     * Initializes and displays the Selected Course view.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * list of attendance checks, attendance percentage and action buttons.</p>
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
        courseController.updateViewTitle();

        center.setTop(titleBar);

        // HBox for the two columns:
        HBox columns = new HBox();
        columns.getStyleClass().add("courseColumns");

        // VBox for the course info and buttons:
        VBox courseInfoBox = new VBox(70);
        courseInfoBox.getStyleClass().add("courseInfoBox");
        HBox.setHgrow(courseInfoBox, Priority.ALWAYS);
        courseInfoBox.setMaxWidth(Double.MAX_VALUE);

        HBox goBackBtnBox = new HBox();
        goBackBtnBox.getStyleClass().add("goBackBtnBox");
        Button goBackButton = new Button(I18nManager.getResourceBundle().getString("general.button.goback"));
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(actionEvent -> {
            try {
                AllCoursesView allCoursesView = new AllCoursesView(primaryStage);
                allCoursesView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to go back a page.", e);
            }
        });

        goBackBtnBox.getChildren().add(goBackButton);

        VBox courseLabels = new VBox();
        courseLabels.setAlignment(Pos.CENTER);
        courseNameLabel.getStyleClass().add("courseNameLabel");
        courseIdentLabel.getStyleClass().add("courseIdentLabel");
        courseLabels.getChildren().addAll(courseNameLabel, courseIdentLabel);

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        courseAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, courseAttendPercentage);
        Label coursePercentageLabel = new Label(I18nManager.getResourceBundle().getString("general.label.percentage"));
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        VBox courseButtons = new VBox(20);
        courseButtons.setAlignment(Pos.CENTER);

        Button generateCourseReportBtn = new Button(I18nManager.getResourceBundle().getString("selectedcourse.button.report"));
        generateCourseReportBtn.getStyleClass().add("courseReportBtn");
        generateCourseReportBtn.setOnAction(actionEvent -> {
            try {
                CourseAttendanceReportView reportView = new CourseAttendanceReportView(primaryStage, courseId);
                reportView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to open a course report view.", e);
            }
        });

        Button showCourseStudentsBtn = new Button(I18nManager.getResourceBundle().getString("selectedcourse.button.students"));
        showCourseStudentsBtn.getStyleClass().add("studentsButton");
        showCourseStudentsBtn.setOnAction(actionEvent -> {
            try {
                SelectedCourseStudentsView studentsView = new SelectedCourseStudentsView(primaryStage, courseId);
                studentsView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to open the view showing course's students.", e);
            }
        });

        courseButtons.getChildren().addAll(generateCourseReportBtn, showCourseStudentsBtn);

        courseInfoBox.getChildren().addAll(goBackBtnBox, courseLabels, attendancePercentageDisplay, courseButtons);
        courseController.updateCourseInfo();

        // VBox for the course attendance checks:
        VBox courseAttendanceChecksBox = new VBox(20);
        courseAttendanceChecksBox.getStyleClass().add("courseAttendanceChecksBox");
        HBox.setHgrow(courseAttendanceChecksBox, Priority.ALWAYS);
        courseAttendanceChecksBox.setMaxWidth(Double.MAX_VALUE);

        Label attendanceChecksLabel = new Label(I18nManager.getResourceBundle().getString("selectedcourse.checks.title").toUpperCase());
        attendanceChecksLabel.getStyleClass().add("attendanceChecksLabel");

        attendanceChecksList.getStyleClass().add("attendanceChecksList");
        attendanceChecksList.setSpacing(8);
        ScrollPane attendanceChecksBox = new ScrollPane(attendanceChecksList);
        attendanceChecksBox.getStyleClass().add("attendanceChecksBox");
        courseController.displayAttendanceChecks();

        Button createCheckButton = new Button(I18nManager.getResourceBundle().getString("selectedcourse.button.createcheck"));
        createCheckButton.getStyleClass().add("createCheckButton");

        createCheckButton.setOnAction(actionEvent -> {
            try {
                courseController.createNewAttendanceCheck();
                courseController.displayAttendanceChecks();
                courseController.updateCourseInfo();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to create a new attendance check", e);
            }
        });

        courseAttendanceChecksBox.getChildren().addAll(attendanceChecksLabel, attendanceChecksBox, createCheckButton);

        columns.getChildren().addAll(courseInfoBox, courseAttendanceChecksBox);
        center.setCenter(columns);

        columns.setMaxWidth(Double.MAX_VALUE);
        center.setMaxWidth(Double.MAX_VALUE);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        viewBasicLayout.setMaxWidth(Double.MAX_VALUE);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/selectedcourse_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.selectedcourse"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
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
     * Display the identifier and name of the course on the UI.
     * @param courseIdent the identifier of the course
     * @param courseName the name of the course
     */
    public void displayCourseNameAndIdentifier(String courseName, String courseIdent) {
        courseNameLabel.setText(courseName);
        courseIdentLabel.setText(courseIdent);
    }

    /**
     * Display the attendance percentage of the course.
     * @param percentage the attendance percentage of the course
     */
    public void displayCourseAttendancePercentage(int percentage) {
        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());
        courseAttendPercentage.setText(nf.format(percentage/100.0));
    }

    /**
     * Adds an attendance check entry to the UI list.
     *
     * @param date the date of the attendance check
     * @param time the time of the attendance check
     * @param percentage the attendance percentage of the attendance check
     * @param attendanceCheckId the id of the attendance check
     */
    public void addToAttendanceChecksList(LocalDate date, LocalTime time, int percentage, int attendanceCheckId) {
        HBox attendanceCheckInsert = new HBox();
        attendanceCheckInsert.getStyleClass().add("attendanceCheckItem");

        Button attendanceCheckSelector = new Button();
        attendanceCheckSelector.getStyleClass().add("attendanceCheckSelector");

        attendanceCheckSelector.setOnAction(actionEvent -> {
            try {
                SelectedAttendanceCheckView attendanceCheckView = new SelectedAttendanceCheckView(primaryStage, attendanceCheckId, courseId);
                attendanceCheckView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to open an attendance check view.", e);
            }
        });

        HBox attendanceCheckInfo = new HBox();

        HBox dateAndTime = new HBox(30);
        dateAndTime.getStyleClass().add("dateAndTime");

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        String formattedDate = date.format(formatter);
        Label checkDate = new Label(formattedDate);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(I18nManager.getCurrentLocale());
        String formattedTime = time.format(formatter2);
        Label checkTime = new Label(formattedTime);

        dateAndTime.getChildren().addAll(checkDate, checkTime);

        HBox checkPercentageBox = new HBox();
        checkPercentageBox.getStyleClass().add("attendanceCheckPercentageBox");
        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());
        Label checkPercentage = new Label(nf.format(percentage/100.0));
        checkPercentage.getStyleClass().add("attendanceCheckPercentage");
        checkPercentageBox.getChildren().add(checkPercentage);

        attendanceCheckInfo.getChildren().addAll(checkPercentageBox, dateAndTime);
        HBox.setHgrow(dateAndTime, Priority.ALWAYS);
        dateAndTime.setMaxWidth(Double.MAX_VALUE);
        attendanceCheckSelector.setGraphic(attendanceCheckInfo);

        Button deleteCheckButton = new Button("X");
        deleteCheckButton.getStyleClass().add("deleteCheckButton");

        deleteCheckButton.setOnAction(actionEvent -> {
            try {
                courseController.deleteAttendanceCheck(attendanceCheckId);
                courseController.displayAttendanceChecks();
                courseController.updateCourseInfo();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to delete an attendance check.", e);
            }
        });

        attendanceCheckInsert.getChildren().addAll(attendanceCheckSelector, deleteCheckButton);
        HBox.setHgrow(attendanceCheckSelector, Priority.ALWAYS);
        attendanceCheckSelector.setMaxWidth(Double.MAX_VALUE);

        attendanceChecksList.getChildren().add(attendanceCheckInsert);
    }

    /**
     * Clears the attendance checks list UI.
     */
    public void clearAttendanceChecksList() {
        attendanceChecksList.getChildren().clear();
    }
}
