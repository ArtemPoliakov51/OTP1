package view;

import controller.LoginController;
import i18n.I18nManager;
import i18n.SupportedLocale;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Locale;


public class LoginView extends Application {

    private Stage primaryStage;
    private LoginController loginController;

    private TextField loginEmailField = new TextField();
    private PasswordField loginPasswordField = new PasswordField();

    private Label errorMessage = new Label();

    @Override
    public void start(Stage stage) throws Exception {
        I18nManager.setLocale(new Locale("en", "US"));
        openLoginView(stage);
    }

    public void openLoginView(Stage stage) {
        this.primaryStage = stage;
        this.loginController = LoginController.getInstance();
        this.loginController.setLoginView(this);

        BorderPane loginLayout = new BorderPane();
        VBox titleBox = new VBox();
        VBox loginBox = new VBox();

        titleBox.setId("loginTitleBox");
        loginBox.setId("loginBox");

        Label loginTitle = new Label("ATTENDANCE CHECKER");
        loginTitle.setId("loginTitle");
        titleBox.getChildren().add(loginTitle);

        Label loginLabel = new Label(I18nManager.getResourceBundle().getString("login.title").toUpperCase());
        Label loginEmailLabel = new Label(I18nManager.getResourceBundle().getString("login.email.label"));
        Label loginPasswordLabel = new Label(I18nManager.getResourceBundle().getString("login.password.label"));

        loginLabel.setId("loginLabel");
        loginEmailLabel.getStyleClass().add("loginFieldLabel");
        loginPasswordLabel.getStyleClass().add("loginFieldLabel");

        loginEmailField.setPromptText(I18nManager.getResourceBundle().getString("login.email.prompt"));
        loginPasswordField.setPromptText(I18nManager.getResourceBundle().getString("login.password.prompt"));

        loginEmailField.getStyleClass().add("loginTextField");
        loginPasswordField.getStyleClass().add("loginTextField");

        Button loginButton = new Button(I18nManager.getResourceBundle().getString("login.button.main"));
        loginButton.setId("loginButton");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    loginController.tryLogin();
                    AllCoursesView allCoursesView = new AllCoursesView(primaryStage);
                    allCoursesView.openAllCoursesView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.setId("loginLanguageButton");

        languageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    openLanguageSelectionWindow();
                    //Reload view when window is closed
                    openLoginView(primaryStage);
                    errorMessage.setText("");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        errorMessage.getStyleClass().add("error");

        loginBox.getChildren().addAll(loginLabel, loginEmailLabel, loginEmailField,
                loginPasswordLabel, loginPasswordField, loginButton, errorMessage, languageButton);


        VBox.setMargin(loginEmailField, new Insets(5, 0, 20, 0));
        VBox.setMargin(loginPasswordField, new Insets(5, 0, 20, 0));
        VBox.setMargin(loginButton, new Insets(15, 0, 0, 0));

        loginLayout.setTop(titleBox);
        loginLayout.setCenter(loginBox);

        Scene scene = new Scene(loginLayout, 850, 500);
        scene.getStylesheets().add("/login_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.login"));
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
    }

    public String getLoginEmailValue() {
        return loginEmailField.getText();
    }

    public String getLoginPasswordValue() {
        return loginPasswordField.getText();
    }

    public void displayErrorMessage(String error) {
        errorMessage.setText(error);
    }

    public void openLanguageSelectionWindow() {
        Stage selectionStage = new Stage();
        VBox mainContent = new VBox(10);
        Label title = new Label(I18nManager.getResourceBundle().getString("general.label.selectLang"));

        VBox languageList = new VBox(5);
        ScrollPane languageListBox = new ScrollPane(languageList);

        SupportedLocale[] locales = I18nManager.getAllLocales();
        for (SupportedLocale locale : locales) {
            Button selectLangBtn = new Button(locale.getName());
            selectLangBtn.getStyleClass().add("selectLangBtn");
            selectLangBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        I18nManager.setLocale(new Locale(locale.getLangAbbreviation(), locale.getCountryAbbreviation()));
                        selectionStage.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });
            languageList.getChildren().add(selectLangBtn);
        }

        mainContent.getChildren().addAll(title, languageListBox);

        Scene scene = new Scene(mainContent, 300, 500);
        scene.getStylesheets().add("/language_selection_style.css");
        selectionStage.setTitle(I18nManager.getResourceBundle().getString("window.language"));
        selectionStage.setScene(scene);
        selectionStage.showAndWait();
    }
}
