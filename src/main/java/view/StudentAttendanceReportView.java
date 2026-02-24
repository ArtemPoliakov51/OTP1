package view;

import controller.LoginController;
import controller.StudentAttendanceReportController;
import entity.Teacher;
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
import java.time.LocalDate;
import java.time.LocalTime;

public class StudentAttendanceReportView {
    private Stage primaryStage;
    private StudentAttendanceReportController controller;
    private int courseId;

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private Label studentReportCourseNameLabel = new Label();
    private Label studentReportNameLabel =  new Label();
    private Label studentReportIdLabel =  new Label();

    private Label studentAttendPercentage = new Label();
    private VBox studentReportLines = new VBox(5);
    private VBox absencesList = new VBox();

    protected StudentAttendanceReportView(Stage primaryStage, int courseId, int studentId) {
        this.primaryStage = primaryStage;
        this.controller = new StudentAttendanceReportController(this, courseId, studentId);
        this.courseId = courseId;
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
    }

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

        teacherLabel.setText(teacher.getFirstname().toUpperCase() + " " + teacher.getLastname().toUpperCase());
        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.setText(teacher.getEmail());
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);

        Button homeButton = new Button("HOME");
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

        Button logoutButton = new Button("LOG OUT");
        logoutButton.getStyleClass().add("logoutButton");
        leftSideBarBottom.getChildren().addAll(homeButton, logoutButton);

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

        Button saveReportBtn = new Button("Save");
        saveReportBtn.getStyleClass().add("saveReportButton");

        saveReportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Save report as a txt file?
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    directoryChooser.setTitle("Choose destination folder");
                    directoryChooser.setInitialDirectory(new File("C:/"));
                    File selectedDirectory = directoryChooser.showDialog(new Stage());
                    controller.createAndSaveResults(selectedDirectory);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button goBackButton = new Button("Go Back");
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
        Label reportLabel = new Label("ATTENDANCE REPORT");
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
        Label coursePercentageLabel = new Label("Attendance Percentage");
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        studentReportLines.getStyleClass().add("studentReportLines");

        controller.showAttendancePercentage();
        controller.showStudentReportLines();

        middleColumns.getChildren().addAll(studentReportLines, attendancePercentageDisplay);

        VBox absencesBox = new VBox(10);
        absencesBox.getStyleClass().add("absencesBox");
        Label absencesLabel = new Label("ALL ABSENCES");
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
        this.primaryStage.getScene().getStylesheets().add("/studentreport_style.css");
        this.primaryStage.setTitle("Attendance Checker - Course Attendance Report");
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void displayCourseIdentifierAndName(String title, String name) {
        viewTitle.setText(title);
        studentReportCourseNameLabel.setText(name.toUpperCase());
    }

    public void displayStudentInfo(String firstname, String lastname, int id) {
        studentReportNameLabel.setText(firstname.toUpperCase() + " " + lastname.toUpperCase());
        studentReportIdLabel.setText("ID " + id);
    }

    public void displayAttendancePercentage(int percentage) {
        studentAttendPercentage.setText(percentage + "%");
    }

    public void displayStudentReportLines(int checks, int absences, int excuses) {
        Label allChecks = new Label("Total of Attendance Checks: " + checks);
        Label allAbsences = new Label("Total of Absences: " + absences);
        Label allExcuses = new Label("Total of Excused Absences: " + excuses);
        studentReportLines.getChildren().addAll(allChecks, allAbsences, allExcuses);
    }

    public void addToAbsencesList(String status, LocalDate date, LocalTime time) {
        HBox absenceInsert = new HBox();
        absenceInsert.getStyleClass().add("absenceItem");

        HBox statusBox = new HBox(20);
        Label statusLabel = new Label(status);
        statusLabel.getStyleClass().add("statusLabel");
        statusLabel.getStyleClass().add(status.toLowerCase() + "Report");
        statusBox.getChildren().addAll(statusLabel);

        HBox dateTimeBox = new HBox(5);
        dateTimeBox.getStyleClass().add("dateTimeBox");
        Label dateLabel = new Label(date.toString());
        dateLabel.getStyleClass().add("timeLabel");

        String correctMin = time.getMinute() < 10 ? "0" + time.getMinute() : Integer.toString(time.getMinute());
        Label timeLabel = new Label(time.getHour() + ":" + correctMin);
        timeLabel.getStyleClass().add("timeLabel");

        dateTimeBox.getChildren().addAll(dateLabel, timeLabel);

        absenceInsert.getChildren().addAll(statusBox, dateTimeBox);
        HBox.setHgrow(statusBox, Priority.ALWAYS);
        statusBox.setMaxWidth(Double.MAX_VALUE);

        absencesList.getChildren().add(absenceInsert);
    }
}
