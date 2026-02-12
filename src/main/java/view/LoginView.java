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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LoginView extends Application {

    private Stage primaryStage;
    private LoginController loginController;

    private TextField loginEmailField = new TextField();
    private TextField loginPasswordField = new TextField();

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.loginController = new LoginController(this);

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
                    System.out.println("Login worked");
                    AllCoursesView allCoursesView = new AllCoursesView(loginController.getLoggedInTeacher());
                    System.out.println("allCoursesView initialized");
                    allCoursesView.openAllCoursesView(primaryStage);
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
        scene.getStylesheets().add("/login_style.css");
        stage.setTitle("Attendance Checker");
        stage.setScene(scene);
        stage.show();
    }

    public String getLoginEmailValue() {
        return loginEmailField.getText();
    }

    public String getLoginPasswordValue() {
        return loginPasswordField.getText();
    }
}
