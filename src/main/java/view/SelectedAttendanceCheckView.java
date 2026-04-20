package view;

import controller.LoginController;
import controller.SelectedAttendanceCheckController;
import i18n.I18nManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * JavaFX view for displaying a selected attendance check.
 * <p>
 * This view shows information about a single attendance check,
 * including attendance percentage and a list of students
 * with their attendance statuses and notes.
 * </p>
 *
 * <p>
 * The view communicates with {@link SelectedAttendanceCheckController}
 * to load and update data such as student attendance status and notes.
 * </p>
 */
public class SelectedAttendanceCheckView implements UIView {

    /**
     * The primary stage or window of the application.
     */
    private final Stage primaryStage;

    /**
     * The controller for this view.
     */
    private final SelectedAttendanceCheckController checkController;

    /**
     * The selected course ID.
     */
    private final int courseId;

    /**
     * The title label for the view.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label viewTitle = new Label();

    /**
     * The label for selected attendance check's date.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label checkDateLabel = new Label();

    /**
     * The label for selected attendance check's time.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label checkTimeLabel = new Label();

    /**
     * The label for selected attendance check's attendance percentage.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final Label checkAttendPercentage = new Label();

    /**
     * The VBox list for the students on the attendance check.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final VBox studentsList = new VBox();


    /**
     * Constructs the view for a specific attendance check.
     *
     * @param primaryStage the main application stage
     * @param attendanceCheckId the ID of the attendance check to display
     * @param courseId the ID of the associated course
     */
    protected SelectedAttendanceCheckView(Stage primaryStage, int attendanceCheckId, int courseId) {
        this.primaryStage = primaryStage;
        this.checkController = new SelectedAttendanceCheckController(this, attendanceCheckId, courseId);
        this.courseId = courseId;
    }

    /**
     * Builds and displays the selected attendance check view.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * attendance percentage, student list and action buttons.</p>
     *
     */
    public void openView() {
        BorderPane viewBasicLayout = new BorderPane();

        // The common layout for all the view (other than the login):
        VBox topBar = UIComponent.getTopBar();
        LoginController.getInstance().showTeacherInfo();
        AnchorPane leftSideBar = UIComponent.getLeftSideBar(primaryStage, this);

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
        Button goBackButton = new Button(I18nManager.getResourceBundle().getString("general.button.goback"));
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    SelectedCourseView courseView = new SelectedCourseView(primaryStage, courseId);
                    courseView.openView();
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
        Label attCheckLabel = new Label(I18nManager.getResourceBundle().getString("check.title"));
        attCheckLabel.getStyleClass().add("attCheckLabel");
        checkLabels.getChildren().addAll(checkDateLabel, checkTimeLabel, attCheckLabel);

        VBox attendancePercentageDisplay = new VBox();
        attendancePercentageDisplay.setAlignment(Pos.CENTER);
        checkAttendPercentage.getStyleClass().add("attendancePercentageLabel");
        Ellipse attendPercentageOval = new Ellipse(60, 50);
        attendPercentageOval.getStyleClass().add("attendancePercentageOval");
        StackPane percentageStack = new StackPane();
        percentageStack.getChildren().addAll(attendPercentageOval, checkAttendPercentage);
        Label coursePercentageLabel = new Label(I18nManager.getResourceBundle().getString("general.label.percentage"));
        attendancePercentageDisplay.getChildren().addAll(percentageStack, coursePercentageLabel);


        checkInfoBox.getChildren().addAll(goBackBtnBox, checkLabels, attendancePercentageDisplay);
        checkController.updateCheckInfo();

        // VBox for the course attendance checks:
        VBox checkStudentsBox = new VBox(20);
        checkStudentsBox.getStyleClass().add("checkStudentsBox");
        HBox.setHgrow(checkStudentsBox, Priority.ALWAYS);
        checkStudentsBox.setMaxWidth(Double.MAX_VALUE);

        Label studentsLabel = new Label(I18nManager.getResourceBundle().getString("check.title.students"));
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
        this.primaryStage.getScene().getStylesheets().add("/styles/selected_attendancecheck_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.check"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    /**
     * Adds a student entry to the student list UI.
     *
     * @param firstname student's firstname
     * @param lastname student's lastname
     * @param studentId student's ID
     * @param attendanceStatus current attendance status (e.g. PRESENT, ABSENT, EXCUSED)
     * @param notes existing notes for the student
     * @param courseId course ID associated with the attendance check
     */
    public void addToStudentsList(String firstname, String lastname, int studentId, String attendanceStatus, String notes, int courseId) {
        VBox studentInsert = new VBox();
        studentInsert.getStyleClass().add("studentItem");

        HBox studentInfo = new HBox();
        studentInfo.getStyleClass().add("studentInfo");

        HBox studentName = new HBox(5);
        studentName.getStyleClass().add("studentName");
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        Label nameLabel = new Label(firstname + separator + lastname);
        Label idLabel = new Label("ID " + studentId);
        idLabel.getStyleClass().add("idLabel");
        studentName.getChildren().addAll(nameLabel, idLabel);

        Button notesButton = new Button(I18nManager.getResourceBundle().getString("check.button.notes").toUpperCase());
        notesButton.getStyleClass().add("notesButton");

        final boolean[] isNotes = {false};
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
                    if (!isNotes[0]) {
                        noteBox.getStyleClass().remove("hidden");
                        noteArea.getStyleClass().remove("hidden");
                        notesButton.setText(I18nManager.getResourceBundle().getString("check.button.save"));
                        isNotes[0] = true;
                    } else {
                        checkController.saveNote(studentId, noteArea.getText());
                        notesButton.setText(I18nManager.getResourceBundle().getString("check.button.notes"));
                        noteBox.getStyleClass().add("hidden");
                        noteArea.getStyleClass().add("hidden");
                        isNotes[0] = false;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        Button absenceButton = new Button();
        absenceButton.getStyleClass().add("absenceButton");

        boolean[] isExcused = {false};

        if (attendanceStatus.equals("EXCUSED")) {
            absenceButton.setText(I18nManager.getResourceBundle().getString("check.button.excused"));
            absenceButton.getStyleClass().add("excused");
            isExcused[0] = true;
        }
        else if (attendanceStatus.equals("ABSENT")) {
            absenceButton.setText(I18nManager.getResourceBundle().getString("check.button.absent"));
            absenceButton.getStyleClass().add("absent");
            isExcused[0] = false;
        }
        else if (attendanceStatus.equals("PRESENT")) {
            // Set button disabled if student attendance status is "PRESENT"
            absenceButton.setDisable(true);
        }

        absenceButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Change attendance status between ABSENT and EXCUSED
                    isExcused[0] = !isExcused[0];
                    checkController.updateAbsenceStatus(studentId, isExcused[0]);
                    absenceButton.setText(isExcused[0] ?
                            I18nManager.getResourceBundle().getString("check.button.excused") :
                            I18nManager.getResourceBundle().getString("check.button.absent"));
                    absenceButton.getStyleClass().remove(isExcused[0] ? "absent" : "excused");
                    absenceButton.getStyleClass().add(isExcused[0] ? "excused" : "absent");
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
                    checkController.updateStudentStatus(studentId, statusCheck.isSelected());
                    absenceButton.setDisable((statusCheck.isSelected()));
                    absenceButton.setText(statusCheck.isSelected() ? "" : I18nManager.getResourceBundle().getString("check.button.absent"));
                    if (statusCheck.isSelected()) {
                        absenceButton.getStyleClass().remove("absent");
                        absenceButton.getStyleClass().remove("excused");
                    }
                    absenceButton.getStyleClass().add(statusCheck.isSelected() ? "" : "absent");
                    isExcused[0] = false;
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

    /**
     * Displays the view's title on the top of the main content.
     *
     * @param title the title of the view
     */
    public void displayViewTitle(String title) {
        viewTitle.setText(title);
    }

    /**
     * Displays the date and time of the attendance check.
     *
     * @param checksDate date of the check
     * @param checksTime time of the check
     */
    public void displayChecksDateAndTime(LocalDate checksDate, LocalTime checksTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        String formattedDate = checksDate.format(formatter);
        checkDateLabel.setText(formattedDate);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(I18nManager.getCurrentLocale());
        String formattedTime = checksTime.format(formatter2);
        checkTimeLabel.setText(formattedTime);
    }

    /**
     * Displays the attendance percentage of the selected attendance check.
     *
     * @param percentage attendance percentage of the attendance check
     */
    public void displayChecksAttendancePercentage(int percentage) {
        NumberFormat nf = NumberFormat.getPercentInstance(I18nManager.getCurrentLocale());
        checkAttendPercentage.setText(nf.format(percentage/100.0));
    }

    /**
     * Clears all student entries from the UI list. Used when updating the statuses.
     */
    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
