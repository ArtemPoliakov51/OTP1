package view;

import controller.LoginController;
import controller.SelectedCourseController;
import entity.Teacher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SelectedCourseView {

    private Stage primaryStage;
    private SelectedCourseController courseController;

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private Label courseNameLabel = new Label();
    private Label courseIdentLabel = new Label();

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
        VBox courseInfoBox = new VBox();
        courseInfoBox.getStyleClass().add("courseInfoBox");
        HBox.setHgrow(courseInfoBox, Priority.ALWAYS);
        courseInfoBox.setMaxWidth(Double.MAX_VALUE);

        HBox goBackBtnBox = new HBox();
        goBackBtnBox.getStyleClass().add("goBackBtnBox");
        Button goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("goBackButton");
        goBackBtnBox.getChildren().add(goBackButton);

        courseNameLabel.getStyleClass().add("courseNameLabel");
        courseIdentLabel.getStyleClass().add("courseIdentLabel");
        courseController.updateCourseInfo();

        courseInfoBox.getChildren().addAll(goBackBtnBox, courseNameLabel, courseIdentLabel);

        columns.getChildren().add(courseInfoBox);
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
}
