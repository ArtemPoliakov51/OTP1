package view;

import controller.AllCoursesController;
import controller.LoginController;
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

import java.time.LocalDateTime;

public class AllCoursesView {

    private Stage primaryStage;
    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private Label shownCoursesLabel = new Label();
    private Button changeShownButton = new Button();

    private AllCoursesController allCoursesController;
    private VBox coursesList = new VBox();

    protected AllCoursesView(Stage primaryStage) {
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
        this.allCoursesController = new AllCoursesController(this, this.teacher);
        this.primaryStage = primaryStage;
    }

    public void openAllCoursesView() {
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

        // Center content:
        BorderPane center = new BorderPane();
        center.getStyleClass().add("center");

        HBox titleBar = new HBox();
        titleBar.getStyleClass().add("titleBar");
        viewTitle.setText("MY COURSES");
        viewTitle.getStyleClass().add("viewTitle");
        titleBar.getChildren().add(viewTitle);

        center.setTop(titleBar);

        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(20, 50, 20 ,50));

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");
        shownCoursesLabel.setText("ACTIVE");
        shownCoursesLabel.getStyleClass().add("shownCoursesLabel");

        changeShownButton.setText("ARCHIVED");
        changeShownButton.getStyleClass().add("changeShownButton");
        changeShownButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    allCoursesController.displayCourses(changeShownButton.getText());
                    changeLabels();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerRow.getChildren().addAll(shownCoursesLabel, spacer, changeShownButton);

        allCoursesController.displayActiveCourses();

        ScrollPane coursesBox = new ScrollPane(coursesList);
        coursesBox.getStyleClass().add("coursesBox");

        HBox createButtonBox = new HBox();
        createButtonBox.getStyleClass().add("createButtonBox");

        Button createCourseBtn = new Button("CREATE NEW COURSE");
        createCourseBtn.getStyleClass().add("createCourseButton");

        createCourseBtn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open create course view for logged in teacher
                    //CreateCourseView createCourseView = new CreateCourseView(primaryStage, teacher);
                    //createCourseView.showCreateCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        createButtonBox.getChildren().add(createCourseBtn);

        centerBox.getChildren().addAll(headerRow, coursesBox, createButtonBox);
        center.setCenter(centerBox);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        Scene scene = new Scene(viewBasicLayout, 1200, 800);
        scene.getStylesheets().add("/allcourses_style.css");
        primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public void addToActiveCoursesList(String courseIdentifier, String courseName, LocalDateTime created, int courseId) {
        HBox courseInsert = new HBox();
        courseInsert.getStyleClass().add("courseItem");

        Button courseSelector = new Button();
        courseSelector.getStyleClass().add("courseSelector");

        courseSelector.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open selected course view for specific course
                    SelectedCourseView selectedCourseView = new SelectedCourseView(primaryStage, courseId);
                    selectedCourseView.openSelectedCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        HBox courseInfo = new HBox(20);
        Label cIdentifier = new Label(courseIdentifier);
        Label cName = new Label(courseName);
        Label cDate = new Label(created.toString());

        courseInfo.getChildren().addAll(cIdentifier, cName, cDate);
        courseSelector.setGraphic(courseInfo);

        Button archiveCourseButton = new Button("ARCHIVE");
        archiveCourseButton.getStyleClass().add("courseActionButton");

        archiveCourseButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Archive selected course
                    // allCoursesController.archiveCourse(courseId);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        courseInsert.getChildren().addAll(courseSelector, archiveCourseButton);
        HBox.setHgrow(courseSelector, Priority.ALWAYS);
        courseSelector.setMaxWidth(Double.MAX_VALUE);

        coursesList.getChildren().add(courseInsert);
    }

    public void addToArchivedCoursesList(String courseIdentifier, String courseName, LocalDateTime created, LocalDateTime archived, int courseId) {
        HBox courseInsert = new HBox();
        courseInsert.getStyleClass().add("courseItem");

        Button courseSelector = new Button();
        courseSelector.getStyleClass().add("courseSelector");

        courseSelector.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Activate selected course
                    //SelectedCourseView selectedCourseView = new SelectedCourseView(courseId);
                    //selectedCourseView.showSelectedCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        HBox courseInfo = new HBox(20);
        Label cIdentifier = new Label(courseIdentifier);
        Label cName = new Label(courseName);
        Label cDate = new Label(created.toString());
        Label cADate = new Label(archived.toString());

        courseInfo.getChildren().addAll(cIdentifier, cName, cDate, cADate);
        courseSelector.setGraphic(courseInfo);

        Button archiveCourseButton = new Button("ACTIVATE");
        archiveCourseButton.getStyleClass().add("courseActionButton");

        archiveCourseButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open selected course view for specific course
                    // allCoursesController.activateCourse(courseId);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        courseInsert.getChildren().addAll(courseSelector, archiveCourseButton);
        HBox.setHgrow(courseSelector, Priority.ALWAYS);
        courseSelector.setMaxWidth(Double.MAX_VALUE);

        coursesList.getChildren().add(courseInsert);
    }

    public void clearCoursesList() {
        coursesList.getChildren().clear();
    }

    public void changeLabels() {
        String currentShown = shownCoursesLabel.getText();
        String changeLabelTo = currentShown.equals("ACTIVE") ? "ARCHIVED" : "ACTIVE";
        shownCoursesLabel.setText(changeLabelTo);

        String currentText = changeShownButton.getText();
        String changeButtonTo = currentText.equals("ACTIVE") ? "ARCHIVED" : "ACTIVE";
        changeShownButton.setText(changeButtonTo);
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
