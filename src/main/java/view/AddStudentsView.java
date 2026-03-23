package view;

import controller.AddStudentsController;
import controller.LoginController;
import entity.Teacher;
import i18n.I18nManager;
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

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private VBox studentsList = new VBox();

    protected AddStudentsView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.addStudentsController = new AddStudentsController(this, courseId);
        this.courseId = courseId;
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

        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);
        addStudentsController.showTeacherInfo();

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
        addStudentsController.updateViewTitle();

        center.setTop(titleBar);

        // VBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");

        Button addStudentsButton = new Button(I18nManager.getResourceBundle().getString("addstudents.button.add"));
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


        Button goBackButton = new Button(I18nManager.getResourceBundle().getString("general.button.goback"));
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

        Label addStudentsLabel = new Label(I18nManager.getResourceBundle().getString("addstudents.title").toUpperCase());
        addStudentsLabel.getStyleClass().add("addStudentsLabel");
        Label instructions = new Label(I18nManager.getResourceBundle().getString("addstudents.instructions"));
        instructions.getStyleClass().add("instructions");

        VBox listAndSearchContainer = new VBox();

        HBox searchBar = new HBox(10);
        searchBar.getStyleClass().add("searchBar");
        Label searchLabel = new Label(I18nManager.getResourceBundle().getString("addstudents.label.search"));
        searchLabel.getStyleClass().add("searchLabel");
        TextField searchField = new TextField();
        searchField.getStyleClass().add("searchField");
        searchField.setPromptText(I18nManager.getResourceBundle().getString("addstudents.prompt.search"));

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
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.addstudents"));
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
        if (selectedStudentIds.contains(studentId)) {selectStudentCheckBox.setSelected(true);}

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

    public void displayTeacherInfo(String firstname, String lastname, String email) {
        teacherLabel.setText(firstname.toUpperCase() + " " + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
    }

    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
