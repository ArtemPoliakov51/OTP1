package view;

import controller.AllCoursesController;
import controller.LoginController;
import entity.Teacher;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AllCoursesView {

    private Stage primaryStage;
    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private AllCoursesController allCoursesController;
    private VBox coursesList = new VBox();

    protected AllCoursesView(Teacher teacher) {
        this.allCoursesController = new AllCoursesController(this, this.teacher = teacher);
    }

    public void openAllCoursesView(Stage primaryStage) {
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

        VBox centerTitleBar = new VBox();
        centerTitleBar.getStyleClass().add("centerTitleBar");
        viewTitle.setText("MY COURSES");
        viewTitle.getStyleClass().add("viewTitle");
        centerTitleBar.getChildren().add(viewTitle);

        VBox centerContent = new VBox();

        allCoursesController.displayActiveCourses();
        centerContent.getChildren().addAll(centerTitleBar, coursesList);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(centerContent);

        Scene scene = new Scene(viewBasicLayout, 1200, 800);
        scene.getStylesheets().add("/styles.css");
        primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public void addToActiveCoursesList(String courseIdentifier, String courseName, LocalDateTime created, int courseId) {
        HBox courseInsert = new HBox();
        Button courseSelector = new Button();

        HBox courseInfo = new HBox();
        Label cIdentifier = new Label(courseIdentifier);
        Label cName = new Label(courseName);
        Label cDate = new Label(created.toString());

        courseInfo.getChildren().addAll(cIdentifier, cName, cDate);
        courseSelector.setGraphic(courseInfo);

        Button archiveCourseButton = new Button("ARCHIVE");

        courseInsert.getChildren().addAll(courseSelector, archiveCourseButton);
        coursesList.getChildren().add(courseInsert);
    }

    public void updateViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void updateTeacherLabel(String teacherName) {
        teacherLabel.setText(teacherName);
    }

    public void updateTeacherEmailLabel(String teacherEmail) {
        teacherEmailLabel.setText(teacherEmail);
    }
}
