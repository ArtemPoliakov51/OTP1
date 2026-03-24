package view;

import controller.AllCoursesController;
import controller.LoginController;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AllCoursesView {

    private Stage primaryStage;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private Label shownCoursesLabel = new Label();
    private Button changeShownButton = new Button();

    private AllCoursesController allCoursesController;
    private VBox coursesList = new VBox(8);

    private static boolean isActiveCourses = true;

    protected AllCoursesView(Stage primaryStage) {
        this.allCoursesController = new AllCoursesController(this);
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

        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);
        allCoursesController.showTeacherInfo();

        // Just for show in this view, since this is the "Home"-view.
        Button homeButton = new Button(I18nManager.getResourceBundle().getString("general.button.home"));
        homeButton.getStyleClass().add("homeButton");

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

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.getStyleClass().add("languageButton");

        languageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    LanguageSelectorView.openLanguageSelectionWindow();
                    //Reload view when window is closed
                    openAllCoursesView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        leftSideBarBottom.getChildren().addAll(homeButton, languageButton, logoutButton);

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
        viewTitle.setText(I18nManager.getResourceBundle().getString("allcourses.title").toUpperCase());
        viewTitle.getStyleClass().add("viewTitle");
        titleBar.getChildren().add(viewTitle);

        center.setTop(titleBar);

        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(20, 50, 20 ,50));

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");
        shownCoursesLabel.setText(I18nManager.getResourceBundle().getString("allcourses.subtitle.active"));
        shownCoursesLabel.getStyleClass().add("shownCoursesLabel");

        changeShownButton.setText(I18nManager.getResourceBundle().getString("allcourses.button.archived"));
        changeShownButton.getStyleClass().add("changeShownButton");
        changeShownButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    isActiveCourses = !isActiveCourses;
                    allCoursesController.displayCourses(isActiveCourses);
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

        Button createCourseBtn = new Button(I18nManager.getResourceBundle().getString("allcourses.button.create").toUpperCase());
        createCourseBtn.getStyleClass().add("createCourseButton");

        createCourseBtn.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open create course view for logged in teacher
                    CreateCourseView createCourseView = new CreateCourseView(primaryStage);
                    createCourseView.openCreateCourseView();
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

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/allcourses_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.allcourses"));
        this.primaryStage.setMaximized(true);
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

        HBox courseNameBox = new HBox(10);
        courseNameBox.getStyleClass().add("courseNameBox");
        Label cIdentifier = new Label(courseIdentifier);
        cIdentifier.getStyleClass().add("courseIdentifier");
        Label cName = new Label(courseName);
        cName.getStyleClass().add("courseName");
        courseNameBox.getChildren().addAll(cIdentifier, cName);

        // Should these also be formatted to match the locale?
        Label cDate = new Label(created.getDayOfMonth() + "-" + created.getMonthValue() + "-" + created.getYear());
        cDate.getStyleClass().add("courseDate");

        courseInfo.getChildren().addAll(courseNameBox, cDate);
        HBox.setHgrow(courseNameBox, Priority.ALWAYS);
        courseNameBox.setMaxWidth(Double.MAX_VALUE);

        courseSelector.setGraphic(courseInfo);

        Button archiveCourseButton = new Button(I18nManager.getResourceBundle().getString("allcourses.button.archive"));
        archiveCourseButton.getStyleClass().add("courseActionButton");

        archiveCourseButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Archive selected course
                    allCoursesController.archiveCourse(courseId);
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
                    // Open selected course view for specific course
                    SelectedCourseView selectedCourseView = new SelectedCourseView(primaryStage, courseId);
                    selectedCourseView.openSelectedCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        HBox courseInfo = new HBox(20);

        HBox courseNameBox = new HBox(10);

        courseNameBox.getStyleClass().add("courseNameBox");
        Label cIdentifier = new Label(courseIdentifier);
        cIdentifier.getStyleClass().add("courseIdentifier");
        Label cName = new Label(courseName);
        cName.getStyleClass().add("courseName");
        courseNameBox.getChildren().addAll(cIdentifier, cName);

        Label cDate = new Label(created.getDayOfMonth() + "-" + created.getMonthValue() + "-" + created.getYear());
        cDate.getStyleClass().add("courseDate");

        Label cADate = new Label(archived.getDayOfMonth() + "-" + archived.getMonthValue() + "-" + archived.getYear());
        cADate.getStyleClass().add("courseDate");

        courseInfo.getChildren().addAll(courseNameBox, cDate, cADate);
        HBox.setHgrow(courseNameBox, Priority.ALWAYS);
        courseNameBox.setMaxWidth(Double.MAX_VALUE);

        courseSelector.setGraphic(courseInfo);

        Button activateCourseButton = new Button(I18nManager.getResourceBundle().getString("allcourses.button.activate"));
        activateCourseButton.getStyleClass().add("courseActionButton");

        activateCourseButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Activate selected course
                    allCoursesController.activateCourse(courseId);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        courseInsert.getChildren().addAll(courseSelector, activateCourseButton);
        HBox.setHgrow(courseSelector, Priority.ALWAYS);
        courseSelector.setMaxWidth(Double.MAX_VALUE);

        coursesList.getChildren().add(courseInsert);
    }

    public void clearCoursesList() {
        coursesList.getChildren().clear();
    }

    public void changeLabels() {
        String currentShown = shownCoursesLabel.getText();
        String changeLabelTo = currentShown.equals(I18nManager.getResourceBundle().getString("allcourses.subtitle.active")) ?
                I18nManager.getResourceBundle().getString("allcourses.subtitle.archived") : I18nManager.getResourceBundle().getString("allcourses.subtitle.active");
        shownCoursesLabel.setText(changeLabelTo);

        String currentText = changeShownButton.getText();
        String changeButtonTo = currentText.equals(I18nManager.getResourceBundle().getString("allcourses.button.active")) ?
                I18nManager.getResourceBundle().getString("allcourses.button.archived") : I18nManager.getResourceBundle().getString("allcourses.button.active");
        changeShownButton.setText(changeButtonTo);
    }

    public void displayTeacherInfo(String firstname, String lastname, String email) {
        teacherLabel.setText(firstname.toUpperCase() + " " + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
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
