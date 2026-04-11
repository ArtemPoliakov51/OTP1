package view;

import controller.CourseAttendanceReportController;
import controller.LoginController;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

/**
 * JavaFX view for displaying the attendance report for a course.
 *
 * <p>This class is responsible for rendering the UI where a teacher can
 * view the attendance report for selected course. They can also save the course to their computer.</p>
 *
 * <p>The view interacts with {@link CourseAttendanceReportController} to retrieve
 * and update data, and uses {@link I18nManager} for localized UI text.</p>
 */
public class CourseAttendanceReportView {

    /**
     * The primary stage or window of the application.
     */
    private Stage primaryStage;

    /**
     * The controller for this view.
     */
    private CourseAttendanceReportController controller;

    /**
     * The selected course ID.
     */
    private int courseId;

    /**
     * The title label for the view.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label viewTitle = new Label();

    /**
     * The label for teacher's name.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label teacherLabel = new Label();

    /**
     * The label for teacher's email.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label teacherEmailLabel = new Label();

    /**
     * The label for course's name.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label courseNameLabel =  new Label();

    /**
     * The label for course's attendance percentage.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label courseAttendPercentage = new Label();

    /**
     * The VBox list for the report lines.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private VBox reportLines = new VBox(5);

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
    public void openCourseAttendanceReportView() {
        BorderPane viewBasicLayout = new BorderPane();

        // The common layout for all the view (other than the login):
        VBox topBar = new VBox();
        topBar.getStyleClass().add("appTitleBar");
        Label topBarLabel = new Label("ATTENDANCE CHECKER");
        topBarLabel.getStyleClass().add("appTitleBarTitle");
        topBar.getChildren().add(topBarLabel);

        VBox leftSideBarTop = new VBox();
        leftSideBarTop.getStyleClass().add("leftSideBarTop");
        VBox leftSideBarBottom = new VBox();
        leftSideBarBottom.getStyleClass().add("leftSideBarBottom");

        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);
        controller.showTeacherInfo();

        Button homeButton = new Button(I18nManager.getResourceBundle().getString("general.button.home"));
        homeButton.getStyleClass().add("homeButton");
        homeButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Move back to the Home view (AllCoursesView)
                    AllCoursesView allCoursesView = new AllCoursesView(primaryStage);
                    allCoursesView.openAllCoursesView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.getStyleClass().add("languageButton");

        languageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    LanguageSelectorView.openLanguageSelectionWindow();
                    //Reload view when window is closed
                    reportLines.getChildren().clear();
                    openCourseAttendanceReportView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Button logoutButton = new Button(I18nManager.getResourceBundle().getString("general.button.logout"));
        logoutButton.getStyleClass().add("logoutButton");
        logoutButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginController loginController = LoginController.getInstance();
                loginController.logout();
                LoginView loginView = new LoginView();
                loginView.openLoginView(primaryStage);
            }
        });

        leftSideBarBottom.getChildren().addAll(homeButton, languageButton, logoutButton);

        AnchorPane leftSideBar = new AnchorPane();
        leftSideBar.getStyleClass().add("leftSideBar");
        leftSideBar.getChildren().addAll(leftSideBarTop, leftSideBarBottom);
        AnchorPane.setTopAnchor(leftSideBarTop, 20.0);
        AnchorPane.setBottomAnchor(leftSideBarBottom, 20.0);

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

        saveReportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Save report as a txt file?
                    DirectoryChooser directoryChooser = new DirectoryChooser();

                    String exportDir = System.getenv("EXPORT_DIR");
                    if (exportDir != null) {
                        File dir = new File(exportDir);
                        if (dir.exists() && dir.isDirectory()) {
                            directoryChooser.setInitialDirectory(dir);
                        } else {
                            System.out.println("No initial folder was set in .env");
                        }
                    }

                    File selectedDirectory = directoryChooser.showDialog(new Stage());

                    controller.createAndSaveResults(selectedDirectory);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button goBackButton = new Button(I18nManager.getResourceBundle().getString("general.button.goback"));
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    SelectedCourseView selectedCourseView = new SelectedCourseView(primaryStage, courseId);
                    selectedCourseView.openSelectedCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
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
     * Adds report lines collected by the controller to the VBox.
     *
     * @param students the number of students on the course
     * @param checks the number of attendance checks made for the course
     * @param absences the total number of absences on the course
     * @param excuses the total number of excused absences on the course
     * @param lowest the lowest attendance percentage of an attendance check
     * @param lowestDate the date of the attendance check with the lowest attendance percentage
     * @param lowestTime the time of the attendance check with the lowest attendance percentage
     * @param highest the highest attendance percentage of an attendance check
     * @param highestDate the date of the attendance check with the highest attendance percentage
     * @param highestTime the time of the attendance check with the highest attendance percentage
     */
    public void displayCourseReportLines(int students, int checks, int absences, int excuses,
                                         double lowest, LocalDate lowestDate, LocalTime lowestTime,
                                         double highest, LocalDate highestDate, LocalTime highestTime) {
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
        reportLines.getChildren().addAll(allStudents, allChecks, allAbsences, allExcuses, lowestPercentage, highestPercentage);
    }

    /**
     * Displays the teacher's information in the sidebar.
     *
     * @param firstname the teacher's firstname
     * @param lastname the teacher's lastname
     * @param email the teacher's email address
     */
    public void displayTeacherInfo(String firstname, String lastname, String email) {
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        teacherLabel.setText(firstname.toUpperCase() + separator + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
    }
}
