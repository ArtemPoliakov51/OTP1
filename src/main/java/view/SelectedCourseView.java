package view;

import controller.LoginController;
import controller.SelectedCourseController;
import entity.Teacher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class SelectedCourseView {

    private Stage primaryStage;
    private SelectedCourseController courseController;

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private Label courseNameLabel = new Label();
    private Label courseIdentLabel = new Label();
    private Label courseAttendPercentage = new Label();

    private VBox attendanceChecksList = new VBox();

    protected SelectedCourseView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.courseController = new SelectedCourseController(this, courseId);
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
    }

    public void openSelectedCourseView() {
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
        Button goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    AllCoursesView allCoursesView = new AllCoursesView(primaryStage);
                    allCoursesView.openAllCoursesView();
                } catch (Exception e) {
                    System.out.println(e);
                }
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
        Label coursePercentageLabel = new Label("Attendance Percentage");
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        VBox courseButtons = new VBox(20);
        courseButtons.setAlignment(Pos.CENTER);

        Button generateCourseReportBtn = new Button("Course Attendance\nReport");
        generateCourseReportBtn.getStyleClass().add("courseReportBtn");
        generateCourseReportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open course report view
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Button showCourseStudentsBtn = new Button("STUDENTS");
        showCourseStudentsBtn.getStyleClass().add("studentsButton");
        showCourseStudentsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open selected course students view
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        courseButtons.getChildren().addAll(generateCourseReportBtn, showCourseStudentsBtn);

        courseInfoBox.getChildren().addAll(goBackBtnBox, courseLabels, attendancePercentageDisplay, courseButtons);
        courseController.updateCourseInfo();

        // VBox for the course attendance checks:
        VBox courseAttendanceChecksBox = new VBox();
        courseAttendanceChecksBox.getStyleClass().add("courseAttendanceChecksBox");
        HBox.setHgrow(courseAttendanceChecksBox, Priority.ALWAYS);
        courseAttendanceChecksBox.setMaxWidth(Double.MAX_VALUE);

        Label attendanceChecksLabel = new Label("ATTENDANCE CHECKS");
        attendanceChecksLabel.getStyleClass().add("attendanceChecksLabel");

        attendanceChecksList.getStyleClass().add("attendanceChecksList");
        attendanceChecksList.setSpacing(8);
        ScrollPane attendanceChecksBox = new ScrollPane(attendanceChecksList);
        attendanceChecksBox.getStyleClass().add("attendanceChecksBox");
        courseController.displayAttendanceChecks();

        courseAttendanceChecksBox.getChildren().addAll(attendanceChecksLabel, attendanceChecksBox);

        columns.getChildren().addAll(courseInfoBox, courseAttendanceChecksBox);
        center.setCenter(columns);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        Scene scene = new Scene(viewBasicLayout, 1200, 800);
        scene.getStylesheets().add("/selectedcourse_style.css");
        primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public void displayViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void displayCourseNameAndIdentifier(String courseName, String courseIdent) {
        courseNameLabel.setText(courseName);
        courseIdentLabel.setText(courseIdent);
    }

    public void displayCourseAttendancePercentage(int percentage) {
        courseAttendPercentage.setText(percentage + "%");
    }

    public void addToAttendanceChecksList(LocalDate date, LocalTime time, int percentage, int attendanceCheckId) {
        HBox attendanceCheckInsert = new HBox();
        attendanceCheckInsert.getStyleClass().add("attendanceCheckItem");

        Button attendanceCheckSelector = new Button();
        attendanceCheckSelector.getStyleClass().add("attendanceCheckSelector");

        attendanceCheckSelector.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open selected attendance check view for specific course
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        HBox attendanceCheckInfo = new HBox();

        HBox dateAndTime = new HBox(30);
        dateAndTime.getStyleClass().add("dateAndTime");
        Label checkDate = new Label(date.toString());
        Label checkTime = new Label(time.toString());
        dateAndTime.getChildren().addAll(checkDate, checkTime);

        HBox checkPercentageBox = new HBox();
        checkPercentageBox.getStyleClass().add("attendanceCheckPercentageBox");
        Label checkPercentage = new Label(percentage + "%");
        checkPercentage.getStyleClass().add("attendanceCheckPercentage");
        checkPercentageBox.getChildren().add(checkPercentage);

        attendanceCheckInfo.getChildren().addAll(checkPercentageBox, dateAndTime);
        HBox.setHgrow(dateAndTime, Priority.ALWAYS);
        dateAndTime.setMaxWidth(Double.MAX_VALUE);
        attendanceCheckSelector.setGraphic(attendanceCheckInfo);

        Button deleteCheckButton = new Button("X");
        deleteCheckButton.getStyleClass().add("deleteCheckButton");

        deleteCheckButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Delete selected attendance check
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        attendanceCheckInsert.getChildren().addAll(attendanceCheckSelector, deleteCheckButton);
        HBox.setHgrow(attendanceCheckSelector, Priority.ALWAYS);
        attendanceCheckSelector.setMaxWidth(Double.MAX_VALUE);

        attendanceChecksList.getChildren().add(attendanceCheckInsert);
    }

    public void clearAttendanceChecksList() {
        attendanceChecksList.getChildren().clear();
    }
}
