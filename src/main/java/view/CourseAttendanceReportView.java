package view;

import controller.CourseAttendanceReportController;
import controller.LoginController;
import entity.Teacher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

public class CourseAttendanceReportView {

    private Stage primaryStage;
    private CourseAttendanceReportController controller;
    private int courseId;

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();
    private Label courseNameLabel =  new Label();
    private Label courseAttendPercentage = new Label();
    private VBox reportLines = new VBox(5);

    protected CourseAttendanceReportView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.controller = new CourseAttendanceReportController(this, courseId);
        this.courseId = courseId;
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
    }

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

        teacherLabel.setText(teacher.getFirstname().toUpperCase() + " " + teacher.getLastname().toUpperCase());
        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.setText(teacher.getEmail());
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);

        Button homeButton = new Button("HOME");
        homeButton.getStyleClass().add("homeButton");

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
        Label reportLabel = new Label("ATTENDANCE REPORT");
        reportLabel.getStyleClass().add("reportLabel");
        controller.updateViewInfo();

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        courseAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, courseAttendPercentage);
        Label coursePercentageLabel = new Label("Attendance Percentage");
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        reportLines.getStyleClass().add("reportLines");

        controller.showAttendancePercentage();
        controller.showReportLines();

        courseReportBox.getChildren().addAll(courseNameLabel, reportLabel, attendancePercentageDisplay, reportLines);

        content.getChildren().addAll(headerRow, courseReportBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/coursereport_style.css");
        this.primaryStage.setTitle("Attendance Checker - Course Attendance Report");
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void displayCourseIdentifierAndName(String title, String courseName) {
        viewTitle.setText(title);
        courseNameLabel.setText(courseName.toUpperCase());
    }

    public void displayCourseAttendancePercentage(int percentage) {
        courseAttendPercentage.setText(percentage + "%");
    }

    public void displayReportLines(int students, int checks, int absences, int excuses, double lowest, LocalDate lowestDate, LocalTime lowestTime, double highest, LocalDate highestDate, LocalTime highestTime) {
        Label allStudents = new Label("Total of Students: " + students);
        Label allChecks = new Label("Total of Attendance Checks: " + checks);
        Label allAbsences = new Label("Total of Absences: " + absences);
        Label allExcuses = new Label("Total of Excused Absences: " + excuses);
        Label lowestPercentage = new Label("Lowest Attendance Percentage: " + lowest + "%  " + lowestDate + "  " + lowestTime);
        Label highestPercentage = new Label("Highest Attendance Percentage: " + highest + "%  " + highestDate + "  " + highestTime);
        reportLines.getChildren().addAll(allStudents, allChecks, allAbsences, allExcuses, lowestPercentage, highestPercentage);
    }
}
