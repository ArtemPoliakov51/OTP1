package view;

import controller.CreateCourseController;
import controller.LoginController;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CreateCourseView {
    private Stage primaryStage;
    private CreateCourseController createCourseController;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    protected CreateCourseView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.createCourseController = new CreateCourseController(this);
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

        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.getStyleClass().add("teacherEmailLabel");
        leftSideBarTop.getChildren().addAll(teacherLabel, teacherEmailLabel);
        createCourseController.showTeacherInfo();

        Button homeButton = new Button(I18nManager.getResourceBundle().getString("general.button.home"));
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

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.getStyleClass().add("languageButton");

        languageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    LanguageSelectorView.openLanguageSelectionWindow();
                    //Reload view when window is closed
                    openCreateCourseView();
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

        leftSideBarBottom.getChildren().addAll(homeButton, languageButton, logoutButton);

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
        viewTitle.setText(I18nManager.getResourceBundle().getString("createnewcourse.title").toUpperCase());

        center.setTop(titleBar);

        // VBox for the content:
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

        Label creationInstructions = new Label(I18nManager.getResourceBundle().getString("createnewcourse.instructions"));
        creationInstructions.getStyleClass().add("creationInstructions");

        VBox inputFieldBox = new VBox(5);
        inputFieldBox.getStyleClass().add("inputFieldBox");

        Label courseNameInputLabel = new Label(I18nManager.getResourceBundle().getString("createnewcourse.name.label"));
        courseNameInputLabel.getStyleClass().add("courseCreationLabel");

        Label courseIdentInputLabel = new Label(I18nManager.getResourceBundle().getString("createnewcourse.identifier.label"));
        courseIdentInputLabel.getStyleClass().add("courseCreationLabel");

        TextField courseNameField = new TextField();
        courseNameField.setPromptText(I18nManager.getResourceBundle().getString("createnewcourse.name.prompt"));
        courseNameField.getStyleClass().add("courseCreationField");

        TextField courseIdentField = new TextField();
        courseIdentField.setPromptText(I18nManager.getResourceBundle().getString("createnewcourse.identifier.prompt"));
        courseIdentField.getStyleClass().add("courseCreationField");

        inputFieldBox.getChildren().addAll(courseNameInputLabel, courseNameField, courseIdentInputLabel, courseIdentField);

        Button createCourseButton = new Button(I18nManager.getResourceBundle().getString("createnewcourse.button.create").toUpperCase());
        createCourseButton.getStyleClass().add("createButton");

        createCourseButton.setOnAction(actionEvent -> {
                    String courseName = courseNameField.getText() != null ? courseNameField.getText().trim() : "";
                    String courseIdent = courseIdentField.getText() != null ? courseIdentField.getText().trim() : "";

                    if (courseName.isEmpty() || courseIdent.isEmpty()) {
                        new Alert(AlertType.WARNING, I18nManager.getResourceBundle().getString("createnewcourse.error.empty")).showAndWait();
                        return;
                    }

                    /*
                    boolean created = createCourseController.createACourse(courseName, courseIdent);

                    if (created) {
                        //Success: show dialog, then go back to the list
                        Alert ok = new Alert(AlertType.INFORMATION);
                        ok.setTitle(I18nManager.getResourceBundle().getString("createnewcourse.success.title"));
                        ok.setHeaderText(null);
                        ok.setContentText(I18nManager.getResourceBundle().getString("createnewcourse.success1") + courseName + I18nManager.getResourceBundle().getString("createnewcourse.success2"));
                        ok.showAndWait();

                        // Navigate back to AllCoursesView after the user closes the dialog
                        AllCoursesView allCoursesView = new AllCoursesView(primaryStage);
                        allCoursesView.openAllCoursesView();
                    } else {
                        //Failure: show an error message
                        Alert err = new Alert(AlertType.ERROR);
                        err.setTitle(I18nManager.getResourceBundle().getString("createnewcourse.error.title"));
                        err.setHeaderText(null);
                        err.setContentText(I18nManager.getResourceBundle().getString("createnewcourse.error.invalid"));
                        err.showAndWait();
                    }
                     */
                });


            creationFormBox.getChildren().addAll(creationInstructions, inputFieldBox, createCourseButton);

        content.getChildren().addAll(goBackBtnBox, creationFormBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/createcourse_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.createnewcourse"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void displayTeacherInfo(String firstname, String lastname, String email) {
        teacherLabel.setText(firstname.toUpperCase() + " " + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
    }
}
