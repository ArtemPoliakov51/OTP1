package view;

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


public class CheckerGUI extends Application {

    private Stage primaryStage;
    private HomeView homeView;

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

        TextField loginEmailField = new TextField();
        loginEmailField.setPromptText("Enter email");
        TextField loginPasswordField = new TextField();
        loginPasswordField.setPromptText("Enter password");

        loginEmailField.getStyleClass().add("loginTextField");
        loginPasswordField.getStyleClass().add("loginTextField");

        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Login functionalities here (controller check email and password)
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
        scene.getStylesheets().add("/login_style.css");
        stage.setTitle("Attendance Checker");
        stage.setScene(scene);
        stage.show();
    }

    private void openHomeView() {
        this.homeView = new HomeView();
        Scene scene = new Scene(this.homeView.getHomeView(), 850, 500);
        scene.getStylesheets().add("/home_style.css");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }
}
