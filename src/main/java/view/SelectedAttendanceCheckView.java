package view;

import controller.LoginController;
import controller.SelectedAttendanceCheckController;
import entity.Teacher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class SelectedAttendanceCheckView {

    private Stage primaryStage;
    private SelectedAttendanceCheckController checkController;
    private int checkId;
    private int courseId;

    private Teacher teacher;

    private Label viewTitle = new Label();
    private Label teacherLabel = new Label();
    private Label teacherEmailLabel = new Label();

    private Label checkDateLabel = new Label();
    private Label checkTimeLabel = new Label();
    private Label checkAttendPercentage = new Label();

    private VBox studentsList = new VBox();

    protected SelectedAttendanceCheckView(Stage primaryStage, int attendanceCheckId, int courseId) {
        this.primaryStage = primaryStage;
        this.checkController = new SelectedAttendanceCheckController(this, attendanceCheckId, courseId);
        this.checkId = attendanceCheckId;
        this.teacher = LoginController.getInstance().getLoggedInTeacher();
        this.courseId = courseId;
    }

    public void openSelectedAttendanceCheckView() {
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
        checkController.updateViewTitle();

        center.setTop(titleBar);

        // HBox for the two columns:
        HBox columns = new HBox();
        columns.getStyleClass().add("checkColumns");

        // VBox for the course info and buttons:
        VBox checkInfoBox = new VBox(70);
        checkInfoBox.getStyleClass().add("checkInfoBox");
        HBox.setHgrow(checkInfoBox, Priority.ALWAYS);
        checkInfoBox.setMaxWidth(Double.MAX_VALUE);

        HBox goBackBtnBox = new HBox();
        goBackBtnBox.getStyleClass().add("goBackBtnBox");
        Button goBackButton = new Button("Go Back");
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    SelectedCourseView courseView = new SelectedCourseView(primaryStage, courseId);
                    courseView.openSelectedCourseView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        goBackBtnBox.getChildren().add(goBackButton);

        VBox checkLabels = new VBox();
        checkLabels.setAlignment(Pos.CENTER);
        checkDateLabel.getStyleClass().add("checkDateLabel");
        checkTimeLabel.getStyleClass().add("checkTimeLabel");
        Label attCheckLabel = new Label("ATTENDANCE CHECK");
        attCheckLabel.getStyleClass().add("attCheckLabel");
        checkLabels.getChildren().addAll(checkDateLabel, checkTimeLabel, attCheckLabel);

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        checkAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, checkAttendPercentage);
        Label coursePercentageLabel = new Label("Attendance Percentage");
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);


        checkInfoBox.getChildren().addAll(goBackBtnBox, checkLabels, attendancePercentageDisplay);
        checkController.updateCheckInfo();

        // VBox for the course attendance checks:
        VBox checkStudentsBox = new VBox(20);
        checkStudentsBox.getStyleClass().add("checkStudentsBox");
        HBox.setHgrow(checkStudentsBox, Priority.ALWAYS);
        checkStudentsBox.setMaxWidth(Double.MAX_VALUE);

        Label studentsLabel = new Label("STUDENTS");
        studentsLabel.getStyleClass().add("studentsLabel");

        studentsList.getStyleClass().add("studentsList");
        studentsList.setSpacing(8);
        ScrollPane studentListBox = new ScrollPane(studentsList);
        studentListBox.getStyleClass().add("studentListBox");
        checkController.displayStudents();

        checkStudentsBox.getChildren().addAll(studentsLabel, studentListBox);

        columns.getChildren().addAll(checkInfoBox, checkStudentsBox);
        center.setCenter(columns);

        columns.setMaxWidth(Double.MAX_VALUE);
        center.setMaxWidth(Double.MAX_VALUE);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        viewBasicLayout.setMaxWidth(Double.MAX_VALUE);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/selected_attendancecheck_style.css");
        this.primaryStage.setTitle("Attendance Checker - Attendance Check");
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    public void addToStudentsList(String firstname, String lastname, int studentId, String attendanceStatus, String notes, int checksId) {
        VBox studentInsert = new VBox();
        studentInsert.getStyleClass().add("studentItem");

        HBox studentInfo = new HBox();
        studentInfo.getStyleClass().add("studentInfo");

        HBox studentName = new HBox(5);
        studentName.getStyleClass().add("studentName");
        Label firstnameLabel = new Label(firstname);
        Label lastnameLabel = new Label(lastname);
        Label idLabel = new Label("ID " + studentId);
        idLabel.getStyleClass().add("idLabel");
        studentName.getChildren().addAll(firstnameLabel, lastnameLabel, idLabel);

        Button notesButton = new Button("NOTES");
        notesButton.getStyleClass().add("notesButton");

        HBox noteBox = new HBox();
        noteBox.getStyleClass().add("noteBox");
        noteBox.getStyleClass().add("hidden");
        TextArea noteArea = new TextArea(notes);
        noteArea.getStyleClass().add("noteArea");
        noteArea.getStyleClass().add("hidden");
        noteBox.getChildren().add(noteArea);
        HBox.setHgrow(noteArea, Priority.ALWAYS);
        noteArea.setMaxWidth(Double.MAX_VALUE);

        notesButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (notesButton.getText().equals("NOTES")) {
                        noteBox.getStyleClass().remove("hidden");
                        noteArea.getStyleClass().remove("hidden");
                        notesButton.setText("SAVE");
                    } else {
                        checkController.saveNote(checksId, noteArea.getText());
                        notesButton.setText("NOTES");
                        noteBox.getStyleClass().add("hidden");
                        noteArea.getStyleClass().add("hidden");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Button absenceButton = new Button(attendanceStatus);
        absenceButton.getStyleClass().add("absenceButton");
        // Set button disabled if student attendance status is "PRESENT"
        if (attendanceStatus.equals("EXCUSED")) absenceButton.getStyleClass().add("excused");
        if (attendanceStatus.equals("ABSENT")) absenceButton.getStyleClass().add("absent");
        if (attendanceStatus.equals("PRESENT")) absenceButton.setDisable(true);
        absenceButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Change attendance status between ABSENT and EXCUSED
                    checkController.updateAbsenceStatus(checksId, absenceButton.getText());
                    absenceButton.setText(absenceButton.getText().equals("ABSENT") ? "EXCUSED" : "ABSENT");
                    absenceButton.getStyleClass().remove(absenceButton.getText().equals("ABSENT") ? "excused" : "absent");
                    absenceButton.getStyleClass().add(absenceButton.getText().equals("ABSENT") ? "absent" : "excused");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        CheckBox statusCheck = new CheckBox();
        statusCheck.getStyleClass().add("statusCheckBox");
        if (attendanceStatus.equals("PRESENT")) statusCheck.setSelected(true);
        statusCheck.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Change attendance status between PRESENT and ABSENT
                    checkController.updateStudentStatus(checksId, statusCheck.isSelected());
                    absenceButton.setDisable((statusCheck.isSelected()));
                    absenceButton.setText(statusCheck.isSelected() ? "PRESENT" : "ABSENT");
                    if (statusCheck.isSelected()) {
                        absenceButton.getStyleClass().remove("absent");
                        absenceButton.getStyleClass().remove("excused");
                    }
                    absenceButton.getStyleClass().add(statusCheck.isSelected() ? "" : "absent");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        studentInfo.getChildren().addAll(notesButton, studentName, absenceButton, statusCheck);
        HBox.setHgrow(studentName, Priority.ALWAYS);
        studentName.setMaxWidth(Double.MAX_VALUE);

        studentInsert.getChildren().addAll(studentInfo, noteBox);

        studentsList.getChildren().add(studentInsert);
    }

    public void displayViewTitle(String title) {
        viewTitle.setText(title);
    }

    public void displayChecksDateAndTime(String checksDate, String checksTime) {
        checkDateLabel.setText(checksDate);
        checkTimeLabel.setText(checksTime);
    }

    public void displayChecksAttendancePercentage(int percentage) {
        checkAttendPercentage.setText(percentage + "%");
    }

    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
