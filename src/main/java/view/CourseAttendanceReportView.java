package view;

import controller.CourseAttendanceReportController;
import controller.LoginController;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CourseAttendanceReportView {

    private Stage primaryStage;
    private CourseAttendanceReportController controller;
    private int courseId;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();
    private Label courseNameLabel =  new Label();
    private Label courseAttendPercentage = new Label();
    private VBox reportLines = new VBox(5);

    protected CourseAttendanceReportView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.controller = new CourseAttendanceReportController(this, courseId);
        this.courseId = courseId;
    }

    public void openCourseAttendanceReportView() {
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
        controller.showTeacherInfo();

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

        Button languageButton = new Button(I18nManager.getResourceBundle().getString("general.button.language"));
        languageButton.getStyleClass().add("languageButton");

        languageButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    LanguageSelectorView.openLanguageSelectionWindow();
                    //Reload view when window is closed
                    reportLines.getChildren().clear();
                    openCourseAttendanceReportView();
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

        center.setTop(titleBar);

        // VBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");

        Button saveReportBtn = new Button(I18nManager.getResourceBundle().getString("coursereport.button.save"));
        saveReportBtn.getStyleClass().add("saveReportButton");

        saveReportBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Save report as a txt file?
                    DirectoryChooser directoryChooser = new DirectoryChooser();

                    String exportDir = System.getenv("EXPORT_DIR");
                    if (exportDir != null) {
                        File dir = new File(exportDir);
                        if (dir.exists() && dir.isDirectory()) {
                            directoryChooser.setInitialDirectory(dir);
                        } else {
                            System.out.println("No initial folder was set in .env");
                        }
                    }

                    File selectedDirectory = directoryChooser.showDialog(new Stage());

                    controller.createAndSaveResults(selectedDirectory);
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
                    SelectedCourseView selectedCourseView = new SelectedCourseView(primaryStage, courseId);
                    selectedCourseView.openSelectedCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        headerRow.getChildren().addAll(goBackButton, spacer, saveReportBtn);

        // VBox for the course students:
        VBox courseReportBox = new VBox(20);
        courseReportBox.getStyleClass().add("courseReportBox");
        HBox.setHgrow(courseReportBox, Priority.ALWAYS);
        courseReportBox.setMaxWidth(Double.MAX_VALUE);

        courseNameLabel.getStyleClass().add("courseNameLabel");
        Label reportLabel = new Label(I18nManager.getResourceBundle().getString("coursereport.title"));
        reportLabel.getStyleClass().add("reportLabel");
        controller.updateViewInfo();

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        courseAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, courseAttendPercentage);
        Label coursePercentageLabel = new Label(I18nManager.getResourceBundle().getString("general.label.percentage"));
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);

        reportLines.getStyleClass().add("reportLines");

        controller.showAttendancePercentage();
        controller.showCourseReportLines();

        courseReportBox.getChildren().addAll(courseNameLabel, reportLabel, attendancePercentageDisplay, reportLines);

        content.getChildren().addAll(headerRow, courseReportBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/coursereport_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.coursereport"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void displayCourseIdentifierAndName(String title, String courseName) {
        viewTitle.setText(title);
        courseNameLabel.setText(courseName.toUpperCase());
    }

    public void displayCourseAttendancePercentage(int percentage) {
        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());
        courseAttendPercentage.setText(nf.format(percentage/100.0));
    }

    public void displayCourseReportLines(int students, int checks, int absences, int excuses, double lowest, LocalDate lowestDate, LocalTime lowestTime, double highest, LocalDate highestDate, LocalTime highestTime) {
        Label allStudents = new Label(I18nManager.getResourceBundle().getString("coursereport.label.students") + students);
        Label allChecks = new Label(I18nManager.getResourceBundle().getString("coursereport.label.checks") + checks);
        Label allAbsences = new Label(I18nManager.getResourceBundle().getString("coursereport.label.absences") + absences);
        Label allExcuses = new Label(I18nManager.getResourceBundle().getString("coursereport.label.excused") + excuses);

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        String formattedLowestDate = lowestDate.format(formatter);
        String formattedHighestDate = highestDate.format(formatter);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(I18nManager.getCurrentLocale());
        String formattedLowestTime = lowestTime.format(formatter2);
        String formattedHighestTime = highestTime.format(formatter2);

        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());

        Label lowestPercentage = new Label(I18nManager.getResourceBundle().getString("coursereport.label.lowpercentage") + " " + nf.format(lowest/100.0) + "  " + formattedLowestDate + "  " + formattedLowestTime);
        Label highestPercentage = new Label(I18nManager.getResourceBundle().getString("coursereport.label.highpercentage") + " " + nf.format(highest/100.0) + "  " + formattedHighestDate + "  " + formattedHighestTime);
        reportLines.getChildren().addAll(allStudents, allChecks, allAbsences, allExcuses, lowestPercentage, highestPercentage);
    }

    public void displayTeacherInfo(String firstname, String lastname, String email) {
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        teacherLabel.setText(firstname.toUpperCase() + separator + lastname.toUpperCase());
        teacherEmailLabel.setText(email);
    }
}
