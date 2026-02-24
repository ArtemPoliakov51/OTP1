package view;

import controller.CreateCourseController;
import controller.LoginController;
import entity.Teacher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CreateCourseView {
    private Stage primaryStage;
    private CreateCourseController createCourseController;

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    protected CreateCourseView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.createCourseController = new CreateCourseController(this);
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
    }

    public void openCreateCourseView() {
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
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
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
        viewTitle.setText("CREATE A NEW COURSE");

        center.setTop(titleBar);

        // VBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

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

        // VBox for the creation "form":
        VBox creationFormBox = new VBox(30);
        creationFormBox.getStyleClass().add("creationFormBox");
        HBox.setHgrow(creationFormBox, Priority.ALWAYS);
        creationFormBox.setMaxWidth(Double.MAX_VALUE);

        Label creationInstructions = new Label("Give a new course name and a unique identifier\n" +
                "Click \"Create\" button to create the course\n" +
                "Click \"Go Back\" button to return back to view all the courses.");
        creationInstructions.getStyleClass().add("creationInstructions");

        VBox inputFieldBox = new VBox(5);
        inputFieldBox.getStyleClass().add("inputFieldBox");

        Label courseNameInputLabel = new Label("Course name: ");
        courseNameInputLabel.getStyleClass().add("courseCreationLabel");

        Label courseIdentInputLabel = new Label("Course identifier: ");
        courseIdentInputLabel.getStyleClass().add("courseCreationLabel");

        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Enter course name");
        courseNameField.getStyleClass().add("courseCreationField");

        TextField courseIdentField = new TextField();
        courseIdentField.setPromptText("Enter course identifier");
        courseIdentField.getStyleClass().add("courseCreationField");

        inputFieldBox.getChildren().addAll(courseNameInputLabel, courseNameField, courseIdentInputLabel, courseIdentField);

        Button createCourseButton = new Button("CREATE");
        createCourseButton.getStyleClass().add("createButton");

        createCourseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    //createCourseController.createACourse();
                    AllCoursesView allCoursesView = new AllCoursesView(primaryStage);
                    allCoursesView.openAllCoursesView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        creationFormBox.getChildren().addAll(creationInstructions, inputFieldBox, createCourseButton);

        content.getChildren().addAll(goBackBtnBox, creationFormBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/createcourse_style.css");
        this.primaryStage.setTitle("Attendance Checker - Create New Course");
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }
}
