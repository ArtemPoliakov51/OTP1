package view;

import controller.CourseAttendanceReportController;
import controller.LoginController;
import service.I18nManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX view for displaying the attendance report for a course.
 *
 * <p>This class is responsible for rendering the UI where a teacher can
 * view the attendance report for selected course. They can also save the course to their computer.</p>
 *
 * <p>The view interacts with {@link CourseAttendanceReportController} to retrieve
 * and update data, and uses {@link I18nManager} for localized UI text.</p>
 */
public class CourseAttendanceReportView implements UIView {

    /**
     * The primary stage or window of the application.
     */
    private final Stage primaryStage;

    /**
     * The controller for this view.
     */
    private final CourseAttendanceReportController controller;

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
    private final Label courseNameLabel =  new Label();

    /**
     * The label for course's attendance percentage.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label courseAttendPercentage = new Label();

    /**
     * The VBox list for the report lines.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final VBox reportLines = new VBox(5);

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(CourseAttendanceReportView.class.getName());

    /**
     * Constructs the view for displaying the attendance report for the selected course.
     *
     * @param primaryStage the main application stage
     * @param courseId the identifier of the course
     */
    protected CourseAttendanceReportView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.controller = new CourseAttendanceReportController(this, courseId);
        this.courseId = courseId;
    }

    /**
     * Initializes and displays the Course Attendance Report view.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * report lines, attendance percentage and save action button.</p>
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

        center.setTop(titleBar);

        // VBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");

        Button saveReportBtn = new Button(I18nManager.getResourceBundle().getString("coursereport.button.save"));
        saveReportBtn.getStyleClass().add("saveReportButton");

        saveReportBtn.setOnAction(actionEvent -> {
            try {
                // Save report as a txt file?
                DirectoryChooser directoryChooser = new DirectoryChooser();

                String exportDir = System.getenv("EXPORT_DIR");
                if (exportDir != null) {
                    File dir = new File(exportDir);
                    if (dir.exists() && dir.isDirectory()) {
                        directoryChooser.setInitialDirectory(dir);
                    } else {
                        LOGGER.log(Level.WARNING, "No initial folder was set in .env");
                    }
                }

                File selectedDirectory = directoryChooser.showDialog(new Stage());

                controller.createAndSaveResults(selectedDirectory);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to download the course report.", e);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


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

        headerRow.getChildren().addAll(goBackButton, spacer, saveReportBtn);

        // VBox for the course students:
        VBox courseReportBox = new VBox(20);
        courseReportBox.getStyleClass().add("courseReportBox");
        HBox.setHgrow(courseReportBox, Priority.ALWAYS);
        courseReportBox.setMaxWidth(Double.MAX_VALUE);

        courseNameLabel.getStyleClass().add("courseNameLabel");
        Label reportLabel = new Label(I18nManager.getResourceBundle().getString("coursereport.title"));
        reportLabel.getStyleClass().add("reportLabel");
        controller.updateViewInfo();

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        courseAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, courseAttendPercentage);
        Label coursePercentageLabel = new Label(I18nManager.getResourceBundle().getString("general.label.percentage"));
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        reportLines.getStyleClass().add("reportLines");

        controller.showAttendancePercentage();
        controller.showCourseReportLines();

        courseReportBox.getChildren().addAll(courseNameLabel, reportLabel, attendancePercentageDisplay, reportLines);

        content.getChildren().addAll(headerRow, courseReportBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/coursereport_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.coursereport"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    /**
     * Display the identifier as a tile and name of the course on the UI.
     * @param title the identifier of the course to be displayed as a title
     * @param courseName the name of the course
     */
    public void displayCourseIdentifierAndName(String title, String courseName) {
        viewTitle.setText(title);
        courseNameLabel.setText(courseName.toUpperCase());
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
     * Adds the first part of the report lines collected by the controller to the VBox.
     *
     * @param students the number of students on the course
     * @param checks the number of attendance checks made for the course
     * @param absences the total number of absences on the course
     * @param excuses the total number of excused absences on the course
     */
    public void displayCourseReportLinesPartOne(int students, int checks, int absences, int excuses) {
        reportLines.getChildren().clear();

        Label allStudents = new Label(I18nManager
                .getResourceBundle()
                .getString("coursereport.label.students") + students);
        Label allChecks = new Label(I18nManager
                .getResourceBundle()
                .getString("coursereport.label.checks") + checks);
        Label allAbsences = new Label(I18nManager
                .getResourceBundle()
                .getString("coursereport.label.absences") + absences);
        Label allExcuses = new Label(I18nManager
                .getResourceBundle()
                .getString("coursereport.label.excused") + excuses);
        reportLines.getChildren().addAll(allStudents, allChecks, allAbsences, allExcuses);
    }


    /**
     * Adds the second part of the report lines collected by the controller to the VBox.
     *
     * @param lowestDate the date of the attendance check with the lowest attendance percentage
     * @param lowestTime the time of the attendance check with the lowest attendance percentage
     * @param highest the highest attendance percentage of an attendance check
     * @param highestDate the date of the attendance check with the highest attendance percentage
     * @param highestTime the time of the attendance check with the highest attendance percentage
     */
    public void displayCourseReportLinesPartTwo(double lowest, LocalDate lowestDate, LocalTime lowestTime,
                                         double highest, LocalDate highestDate, LocalTime highestTime) {

        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(I18nManager.getCurrentLocale());

        DateTimeFormatter formatter2 = DateTimeFormatter
                .ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(I18nManager.getCurrentLocale());

        String localizedLowestDate = lowestDate != null ? lowestDate.format(formatter) : "";
        String localizedHighestDate = highestDate != null ? highestDate.format(formatter) : "";

        String localizedLowestTime = lowestTime != null ? lowestTime.format(formatter2) : "";
        String localizedHighestTime = highestTime != null ? highestTime.format(formatter2) : "";

        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());

        Label lowestPercentage = new Label(I18nManager.getResourceBundle().getString("coursereport.label.lowpercentage") + " " + nf.format(lowest/100.0) + "  " + localizedLowestDate + "  " + localizedLowestTime);
        Label highestPercentage = new Label(I18nManager.getResourceBundle().getString("coursereport.label.highpercentage") + " " + nf.format(highest/100.0) + "  " + localizedHighestDate + "  " + localizedHighestTime);
        reportLines.getChildren().addAll(lowestPercentage, highestPercentage);
    }

}
