package view;

import controller.LoginController;
import controller.SelectedCourseStudentsController;
import entity.Teacher;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SelectedCourseStudentsView {

    private Stage primaryStage;
    private SelectedCourseStudentsController courseStudentsController;
    private int courseId;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private VBox studentsList = new VBox();

    protected SelectedCourseStudentsView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.courseStudentsController = new SelectedCourseStudentsController(this, courseId);
        this.courseId = courseId;
    }

    public void openSelectedCourseStudentsView() {
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
        courseStudentsController.showTeacherInfo();

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
        courseStudentsController.updateViewTitle();

        center.setTop(titleBar);

        // HBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox goBackBtnBox = new HBox();
        goBackBtnBox.getStyleClass().add("goBackBtnBox");
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

        addStudentsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    AddStudentsView addStudentsView = new AddStudentsView(primaryStage, courseId);
                    addStudentsView.openAddStudentsView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        courseStudentsBox.getChildren().addAll(courseStudentsLabel, studentListBox, addStudentsButton);

        content.getChildren().addAll(goBackBtnBox, courseStudentsBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/selectedcourse_students_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.students"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void addToStudentsList(String firstname, String lastname, int studentId) {
        HBox studentInsert = new HBox();
        studentInsert.getStyleClass().add("studentItem");

        HBox studentInfo = new HBox();
        studentInfo.getStyleClass().add("studentInfo");

        HBox studentName = new HBox(5);
        studentName.getStyleClass().add("studentName");
        Label firstnameLabel = new Label(firstname);
        Label lastnameLabel = new Label(lastname);
        studentName.getChildren().addAll(firstnameLabel, lastnameLabel);

        Button generateStudentReportBtn = new Button(I18nManager.getResourceBundle().getString("students.button.report").toUpperCase());
        generateStudentReportBtn.getStyleClass().add("studentReportButton");

        generateStudentReportBtn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    StudentAttendanceReportView reportView = new StudentAttendanceReportView(primaryStage, courseId, studentId);
                    reportView.openStudentAttendanceReportView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        studentInfo.getChildren().addAll(studentName, generateStudentReportBtn);
        HBox.setHgrow(studentName, Priority.ALWAYS);
        studentName.setMaxWidth(Double.MAX_VALUE);

        Button removeStudentButton = new Button("X");
        removeStudentButton.getStyleClass().add("removeStudentButton");

        removeStudentButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    courseStudentsController.removeStudentFromCourse(studentId);
                    courseStudentsController.displayStudents();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        studentInsert.getChildren().addAll(studentInfo, removeStudentButton);
        HBox.setHgrow(studentInfo, Priority.ALWAYS);
        studentInfo.setMaxWidth(Double.MAX_VALUE);

        studentsList.getChildren().add(studentInsert);
    }

    public void displayViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void displayTeacherInfo(String firstname, String lastname, String email) {
        teacherLabel.setText(firstname.toUpperCase() + " " + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
    }

    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
