package view;

import controller.AddStudentsController;
import controller.LoginController;
import entity.Teacher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddStudentsView {

    private Stage primaryStage;
    private AddStudentsController addStudentsController;
    private int courseId;

    private List<Integer> selectedStudentIds = new ArrayList<>();

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private VBox studentsList = new VBox();

    protected AddStudentsView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.addStudentsController = new AddStudentsController(this, courseId);
        this.courseId = courseId;
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
    }

    public void openAddStudentsView() {
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
        addStudentsController.updateViewTitle();

        center.setTop(titleBar);

        // VBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");

        Button addStudentsButton = new Button("Add Selected");
        addStudentsButton.getStyleClass().add("addStudentsButton");

        addStudentsButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    addStudentsController.addStudentsToCourse(selectedStudentIds);
                    // Move back to the student list view:
                    SelectedCourseStudentsView selectedCourseStudentsView = new SelectedCourseStudentsView(primaryStage, courseId);
                    selectedCourseStudentsView.openSelectedCourseStudentsView();
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
                    SelectedCourseStudentsView selectedCourseStudentsView = new SelectedCourseStudentsView(primaryStage, courseId);
                    selectedCourseStudentsView.openSelectedCourseStudentsView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        headerRow.getChildren().addAll(goBackButton, spacer, addStudentsButton);

        // VBox for the course students:
        VBox courseStudentsBox = new VBox(20);
        courseStudentsBox.getStyleClass().add("courseStudentsBox");
        HBox.setHgrow(courseStudentsBox, Priority.ALWAYS);
        courseStudentsBox.setMaxWidth(Double.MAX_VALUE);

        Label addStudentsLabel = new Label("ADD STUDENTS TO COURSE");
        addStudentsLabel.getStyleClass().add("addStudentsLabel");
        Label instructions = new Label("Select all students from the list to be added to the course.\n" +
                "Click \"Add Selected\" button to add all selected students.\n" +
        "Click \"Go Back\" button to return back to the student list.");
        instructions.getStyleClass().add("instructions");

        VBox listAndSearchContainer = new VBox();

        HBox searchBar = new HBox(10);
        searchBar.getStyleClass().add("searchBar");
        Label searchLabel = new Label("Search: ");
        searchLabel.getStyleClass().add("searchLabel");
        TextField searchField = new TextField();
        searchField.getStyleClass().add("searchField");
        searchField.setPromptText("Search by name");

        // Filter students when letters are typed:
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            addStudentsController.filterStudents(newValue);
        });
        searchBar.getChildren().addAll(searchLabel, searchField);

        studentsList.getStyleClass().add("studentsList");
        studentsList.setSpacing(8);
        ScrollPane studentListBox = new ScrollPane(studentsList);
        studentListBox.getStyleClass().add("studentListBox");
        addStudentsController.displayAvailableStudents();

        listAndSearchContainer.getChildren().addAll(searchBar, studentListBox);

        courseStudentsBox.getChildren().addAll(addStudentsLabel, instructions, listAndSearchContainer);

        content.getChildren().addAll(headerRow, courseStudentsBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/addstudents_style.css");
        this.primaryStage.setTitle("Attendance Checker - Add Students");
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void addToStudentList(int studentId, String firstname, String lastname) {
        HBox studentInsert = new HBox();
        studentInsert.getStyleClass().add("studentItem");

        HBox studentInfo = new HBox(20);

        Label studentIdLabel = new Label("ID " + studentId);
        studentIdLabel.getStyleClass().add("studentIdLabel");

        HBox nameBox = new HBox(5);
        nameBox.getStyleClass().add("nameBox");
        Label firstnameLabel = new Label(firstname);
        firstnameLabel.getStyleClass().add("lastnameLabel");
        Label lastnameLabel = new Label(lastname);
        lastnameLabel.getStyleClass().add("lastnameLabel");
        nameBox.getChildren().addAll(firstnameLabel, lastnameLabel);


        studentInfo.getChildren().addAll(studentIdLabel, nameBox);

        CheckBox selectStudentCheckBox = new CheckBox();
        selectStudentCheckBox.getStyleClass().add("selectStudentCheckBox");

        selectStudentCheckBox.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Add or remove student's ID from the list
                try {
                    if (selectStudentCheckBox.isSelected()) {
                        selectedStudentIds.add(studentId);
                    } else {
                        selectedStudentIds.remove(studentId);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        studentInsert.getChildren().addAll(studentInfo, selectStudentCheckBox);
        HBox.setHgrow(studentInfo, Priority.ALWAYS);
        studentInfo.setMaxWidth(Double.MAX_VALUE);

        studentsList.getChildren().add(studentInsert);
    }

    public void displayViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
