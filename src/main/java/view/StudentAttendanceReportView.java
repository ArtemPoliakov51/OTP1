package view;

import controller.LoginController;
import controller.StudentAttendanceReportController;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
 * JavaFX view for displaying a student's attendance report.
 * <p>
 * This view shows detailed statistics for a single student in a course,
 * including attendance percentage and absence/excuse details.
 * It also allows exporting the report to a file.
 * </p>
 *
 * <p>
 * The view communicates with {@link StudentAttendanceReportController}
 * to retrieve and manage report data.
 * </p>
 */
public class StudentAttendanceReportView {

    /**
     * The primary stage or window of the application.
     */
    private Stage primaryStage;

    /**
     * The controller for this view.
     */
    private StudentAttendanceReportController controller;

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
    private Label studentReportCourseNameLabel = new Label();

    /**
     * The label for student's name.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label studentReportNameLabel =  new Label();

    /**
     * The label for student's identifier.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label studentReportIdLabel =  new Label();

    /**
     * The label for course's attendance percentage.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label studentAttendPercentage = new Label();

    /**
     * The VBox list for the report lines.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private VBox studentReportLines = new VBox(5);

    /**
     * The VBox list for the absence entries.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private VBox absencesList = new VBox();

    /**
     * Constructs a student attendance report view.
     *
     * @param primaryStage main application stage
     * @param courseId course ID associated with the report
     * @param studentId student ID for the report
     */
    protected StudentAttendanceReportView(Stage primaryStage, int courseId, int studentId) {
        this.primaryStage = primaryStage;
        this.controller = new StudentAttendanceReportController(this, courseId, studentId);
        this.courseId = courseId;
    }

    /**
     * Initializes and displays the Student Attendance Report view.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * report lines, attendance percentage and save action button.</p>
     */
    public void openStudentAttendanceReportView() {
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
                    absencesList.getChildren().clear();
                    studentReportLines.getChildren().clear();
                    openStudentAttendanceReportView();
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

        Button saveReportBtn = new Button(I18nManager.getResourceBundle().getString("studentreport.button.save"));
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
                    SelectedCourseStudentsView selectedCourseView = new SelectedCourseStudentsView(primaryStage, courseId);
                    selectedCourseView.openSelectedCourseStudentsView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        headerRow.getChildren().addAll(goBackButton, spacer, saveReportBtn);

        // VBox for the course students:
        VBox studentReportBox = new VBox(20);
        studentReportBox.getStyleClass().add("studentReportBox");
        HBox.setHgrow(studentReportBox, Priority.ALWAYS);
        studentReportBox.setMaxWidth(Double.MAX_VALUE);

        VBox studentReportInfo = new VBox(5);
        studentReportInfo.getStyleClass().add("studentReportInfo");
        studentReportCourseNameLabel.getStyleClass().add("studentReportCourseNameLabel");
        studentReportNameLabel.getStyleClass().add("studentReportNameLabel");
        studentReportIdLabel.getStyleClass().add("studentIdLabel");
        Label reportLabel = new Label(I18nManager.getResourceBundle().getString("studentreport.title"));
        reportLabel.getStyleClass().add("reportLabel");
        controller.updateViewInfo();

        studentReportInfo.getChildren().addAll(studentReportCourseNameLabel, studentReportNameLabel, studentReportIdLabel, reportLabel);

        HBox middleColumns = new HBox(90);
        middleColumns.getStyleClass().add("middleColumns");

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        studentAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, studentAttendPercentage);
        Label coursePercentageLabel = new Label(I18nManager.getResourceBundle().getString("general.label.percentage"));
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        studentReportLines.getStyleClass().add("studentReportLines");

        controller.showAttendancePercentage();
        controller.showStudentReportLines();

        middleColumns.getChildren().addAll(studentReportLines, attendancePercentageDisplay);

        VBox absencesBox = new VBox(10);
        absencesBox.getStyleClass().add("absencesBox");
        Label absencesLabel = new Label(I18nManager.getResourceBundle().getString("studentreport.title.absences"));
        absencesLabel.getStyleClass().add("absencesLabel");
        absencesList.getStyleClass().add("absencesList");
        absencesList.setSpacing(8);
        ScrollPane absencesListBox = new ScrollPane(absencesList);
        absencesListBox.getStyleClass().add("absencesListBox");
        controller.showAbsences();
        absencesBox.getChildren().addAll(absencesLabel, absencesListBox);

        studentReportBox.getChildren().addAll(studentReportInfo, middleColumns, absencesBox);

        content.getChildren().addAll(headerRow, studentReportBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/studentreport_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.studentreport"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
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

    /**
     * Display the identifier as a tile and name of the course on the UI.
     * @param title the identifier of the course to be displayed as a title
     * @param name the name of the course
     */
    public void displayCourseIdentifierAndName(String title, String name) {
        viewTitle.setText(title);
        studentReportCourseNameLabel.setText(name.toUpperCase());
    }

    /**
     * Displays the student's information in the UI.
     *
     * @param firstname the student's firstname
     * @param lastname the student's lastname
     * @param id the student's id
     */
    public void displayStudentInfo(String firstname, String lastname, int id) {
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        studentReportNameLabel.setText(firstname.toUpperCase() + separator + lastname.toUpperCase());
        studentReportIdLabel.setText("ID " + id);
    }

    /**
     * Display the student's attendance percentage for the course.
     * @param percentage the attendance percentage of the student
     */
    public void displayAttendancePercentage(int percentage) {
        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());
        studentAttendPercentage.setText(nf.format(percentage/100.0));
    }

    /**
     * Adds report lines collected by the controller to the VBox.
     *
     * @param checks the number of attendance checks student has participated in
     * @param absences the total number of absences on the student
     * @param excuses the total number of excused absences on the student
     */
    public void displayStudentReportLines(int checks, int absences, int excuses) {
        Label allChecks = new Label(I18nManager.getResourceBundle()
                .getString("studentreport.label.checks") + checks);
        Label allAbsences = new Label(I18nManager.getResourceBundle()
                .getString("studentreport.label.absences") + absences);
        Label allExcuses = new Label(I18nManager.getResourceBundle()
                .getString("studentreport.label.excuses") + excuses);
        studentReportLines.getChildren().addAll(allChecks, allAbsences, allExcuses);
    }

    /**
     * Adds an absence entry to the UI list.
     *
     * @param status the type of the absence (ABSENT or EXCUSED).
     * @param date the date of the absence
     * @param time the time of the absence
     */
    public void addToAbsencesList(String status, LocalDate date, LocalTime time) {
        HBox absenceInsert = new HBox();
        absenceInsert.getStyleClass().add("absenceItem");

        HBox statusBox = new HBox(20);
        Label statusLabel = new Label(status.equals("ABSENT") ?
                I18nManager.getResourceBundle().getString("studentreport.label.absent") :
                I18nManager.getResourceBundle().getString("studentreport.label.excused"));
        statusLabel.getStyleClass().add("statusLabel");
        statusLabel.getStyleClass().add(status.toLowerCase() + "Report");
        statusBox.getChildren().addAll(statusLabel);

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        String formattedDate = date.format(formatter);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(I18nManager.getCurrentLocale());
        String formattedTime = time.format(formatter2);

        HBox dateTimeBox = new HBox(5);
        dateTimeBox.getStyleClass().add("dateTimeBox");
        Label dateLabel = new Label(formattedDate);
        dateLabel.getStyleClass().add("timeLabel");
        Label timeLabel = new Label(formattedTime);
        timeLabel.getStyleClass().add("timeLabel");

        dateTimeBox.getChildren().addAll(dateLabel, timeLabel);

        absenceInsert.getChildren().addAll(statusBox, dateTimeBox);
        HBox.setHgrow(statusBox, Priority.ALWAYS);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        absencesList.getChildren().add(absenceInsert);
    }
}
