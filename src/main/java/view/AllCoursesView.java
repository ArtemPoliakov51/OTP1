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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * JavaFX view for displaying all the courses a teacher owns.
 *
 * <p>This class is responsible for rendering the UI where a teacher can
 * browse their active and archived courses. They can also archive and activate courses.</p>
 *
 * <p>The view interacts with {@link AllCoursesController} to retrieve
 * and update data, and uses {@link I18nManager} for localized UI text.</p>
 */
public class AllCoursesView implements UIView {

    /**
     * The primary stage or window of the application.
     */
    private Stage primaryStage;

    /**
     * The title label for the view.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label viewTitle = new Label();

    /**
     * The label for teacher's name.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label teacherLabel = new Label();

    /**
     * The label for teacher's email.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label teacherEmailLabel = new Label();

    /**
     * The label that shows which courses are currently shown ("ACTIVE" or "ARCHIVED").
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label shownCoursesLabel = new Label();

    /**
     * The button that changes which courses are currently shown ("ACTIVE" or "ARCHIVED").
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Button changeShownButton = new Button();

    /**
     * The controller for this view.
     */
    private AllCoursesController allCoursesController;

    /**
     * The VBox list of course that are currently shown.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private VBox coursesList = new VBox(8);

    /**
     * Boolean value used to decide which courses are displayed.
     */
    private static boolean isActiveCourses = true;

    /**
     * Constructs the view for displaying all the courses.
     *
     * @param primaryStage the main application stage
     */
    protected AllCoursesView(Stage primaryStage) {
        this.allCoursesController = new AllCoursesController(this);
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes and displays the All Courses view.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * course list and action buttons.</p>
     */
    public void openView() {
        BorderPane viewBasicLayout = new BorderPane();

        // The common layout for all the view (other than the login):
        VBox topBar = UIComponent.getTopBar();
        LoginController.getInstance().showTeacherInfo();
        AnchorPane leftSideBar = UIComponent.getLeftSideBar(primaryStage, this);

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

        // Disabled since when course is created Teacher would need to add all translations for course name, which is a lot of work.
        // So this feature is frozen, for now.
        /*
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
         */

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

    /**
     * Adds an active course entry to the UI list with action buttons for selecting and archiving the course.
     *
     * @param courseIdentifier the identifier of the course
     * @param courseName the name of the course
     * @param created the creation datetime of the course
     * @param courseId the id of the course used for the actions
     */
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
                    selectedCourseView.openView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        HBox courseInfo = new HBox(20);

        HBox courseNameBox = new HBox(30);
        courseNameBox.getStyleClass().add("courseNameBox");
        Label cIdentifier = new Label(I18nManager.getResourceBundle().getString("general.label.courseCode") + courseIdentifier);
        cIdentifier.getStyleClass().add("courseIdentifier");
        Label cName = new Label(courseName);
        cName.getStyleClass().add("courseName");
        courseNameBox.getChildren().addAll(cIdentifier, cName);

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        String formattedDate = created.toLocalDate().format(formatter);
        Label cDate = new Label(formattedDate);
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

    /**
     * Adds an archived course entry to the UI list with action buttons for selecting and activating the course.
     *
     * @param courseIdentifier the identifier of the course
     * @param courseName the name of the course
     * @param created the creation datetime of the course
     * @param archived the archiving datetime of the course
     * @param courseId the id of the course used for the actions
     */
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
                    selectedCourseView.openView();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        HBox courseInfo = new HBox(20);

        HBox courseNameBox = new HBox(30);

        courseNameBox.getStyleClass().add("courseNameBox");
        Label cIdentifier = new Label(I18nManager.getResourceBundle().getString("general.label.courseCode") + courseIdentifier);
        cIdentifier.getStyleClass().add("courseIdentifier");
        Label cName = new Label(courseName);
        cName.getStyleClass().add("courseName");
        courseNameBox.getChildren().addAll(cIdentifier, cName);

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        String formattedDate = created.toLocalDate().format(formatter);
        Label cDate = new Label(formattedDate);
        cDate.getStyleClass().add("courseDate");

        String formattedDate2 = archived.toLocalDate().format(formatter);
        Label cADate = new Label(formattedDate2);
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

    /**
     * Clears all course entries from the UI list. Used when switching between active and archived courses.
     */
    public void clearCoursesList() {
        coursesList.getChildren().clear();
    }

    /**
     * Changes the UI labels and button texts that tell which courses are currently shown.
     */
    public void changeLabels() {
        String currentShown = shownCoursesLabel.getText();
        String changeLabelTo = currentShown.equals(I18nManager.getResourceBundle().getString("allcourses.subtitle.active"))
                ? I18nManager.getResourceBundle().getString("allcourses.subtitle.archived")
                : I18nManager.getResourceBundle().getString("allcourses.subtitle.active");
        shownCoursesLabel.setText(changeLabelTo);

        String currentText = changeShownButton.getText();
        String changeButtonTo = currentText.equals(I18nManager.getResourceBundle().getString("allcourses.button.active"))
                ? I18nManager.getResourceBundle().getString("allcourses.button.archived")
                : I18nManager.getResourceBundle().getString("allcourses.button.active");
        changeShownButton.setText(changeButtonTo);
    }
}
