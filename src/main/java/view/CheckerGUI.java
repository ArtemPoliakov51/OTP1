package view;

import controller.LoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CheckerGUI extends Application {

    private Stage primaryStage;
    private LoginController loginController;

    private TextField loginEmailField = new TextField();
    private TextField loginPasswordField = new TextField();

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private VBox viewContentBox = new VBox();

    public void init() {
        this.loginController = new LoginController(this);;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;

        BorderPane loginLayout = new BorderPane();
        VBox titleBox = new VBox();
        VBox loginBox = new VBox();

        titleBox.setId("loginTitleBox");
        loginBox.setId("loginBox");

        Label loginTitle = new Label("ATTENDANCE CHECKER");
        loginTitle.setId("loginTitle");
        titleBox.getChildren().add(loginTitle);

        Label loginLabel = new Label("LOGIN");
        Label loginEmailLabel = new Label("Email: ");
        Label loginPasswordLabel = new Label("Password: ");

        loginLabel.setId("loginLabel");
        loginEmailLabel.getStyleClass().add("loginFieldLabel");
        loginPasswordLabel.getStyleClass().add("loginFieldLabel");

        loginEmailField.setPromptText("Enter email");
        loginPasswordField.setPromptText("Enter password");

        loginEmailField.getStyleClass().add("loginTextField");
        loginPasswordField.getStyleClass().add("loginTextField");

        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    loginController.tryLogin();
                    openHomeView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        loginBox.getChildren().addAll(loginLabel, loginEmailLabel, loginEmailField,
                loginPasswordLabel, loginPasswordField, loginButton);


        VBox.setMargin(loginEmailField, new Insets(5, 0, 20, 0));
        VBox.setMargin(loginPasswordField, new Insets(5, 0, 20, 0));
        VBox.setMargin(loginButton, new Insets(15, 0, 20, 0));

        loginLayout.setTop(titleBox);
        loginLayout.setCenter(loginBox);

        Scene scene = new Scene(loginLayout, 850, 500);
        scene.getStylesheets().add("/styles.css");
        stage.setTitle("Attendance Checker");
        stage.setScene(scene);
        stage.show();
    }

    private void openHomeView() {
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

        teacherLabel.setText("JANE DOE");
        teacherLabel.getStyleClass().add("teacherLabel");
        teacherEmailLabel.setText("jane.doe@email.com");
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

        AllCoursesView allCoursesView = new AllCoursesView(this.loginController);

        VBox centerContent = new VBox();
        centerContent.getChildren().addAll(centerTitleBar, allCoursesView.getCoursesContent());

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(centerContent);

        Scene scene = new Scene(viewBasicLayout, 1200, 800);
        scene.getStylesheets().add("/styles.css");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public String getLoginEmailValue() {
        return loginEmailField.getText();
    }

    public String getLoginPasswordValue() {
        return loginPasswordField.getText();
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
