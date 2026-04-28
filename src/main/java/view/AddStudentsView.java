package view;

import controller.AddStudentsController;
import controller.LoginController;
import service.I18nManager;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX view for adding students to a course.
 *
 * <p>This class is responsible for rendering the UI where a teacher can
 * browse, search, and select students to be added to a specific course.</p>
 *
 * <p>The view interacts with {@link AddStudentsController} to retrieve
 * and update data, and uses {@link I18nManager} for localized UI text.</p>
 */
public class AddStudentsView implements UIView {

    /**
     * The main application stage or window.
     */
    private final Stage primaryStage;

    /**
     * The controller for this view.
     */
    private final AddStudentsController addStudentsController;

    /**
     * The selected course ID.
     */
    private final int courseId;

    /**
     * The list of currently selected students to be added to the course.
     */
    private final List<Integer> selectedStudentIds = new ArrayList<>();

    /**
     * The title label for the view.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private Label viewTitle = new Label();

    /**
     * The VBox list of students that are not currently enrolled to the course.
     *
     * <p>Added as an attribute so it can be updated from different methods.</p>
     */
    private final VBox studentsList = new VBox();

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(AddStudentsView.class.getName());

    /**
     * Constructs the view for adding students to a course.
     *
     * @param primaryStage the main application stage
     * @param courseId the identifier of the course
     */
    protected AddStudentsView(Stage primaryStage, int courseId) {
        this.primaryStage = primaryStage;
        this.addStudentsController = new AddStudentsController(this, courseId);
        this.courseId = courseId;
    }

    /**
     * Initializes and displays the Add Students view.
     *
     * <p>This method builds the entire UI layout, including navigation,
     * student list, search functionality, and action buttons.</p>
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
        addStudentsController.updateViewTitle();

        center.setTop(titleBar);

        // VBox for the content:
        VBox content = new VBox(10);
        content.getStyleClass().add("content");

        HBox headerRow = new HBox();
        headerRow.getStyleClass().add("headerRow");

        Button addStudentsButton = new Button(I18nManager.getResourceBundle().getString("addstudents.button.add"));
        addStudentsButton.getStyleClass().add("addStudentsButton");

        addStudentsButton.setOnAction(actionEvent -> {
            try {
                addStudentsController.addStudentsToCourse(selectedStudentIds);
                // Move back to the student list view:
                SelectedCourseStudentsView selectedCourseStudentsView = new SelectedCourseStudentsView(primaryStage, courseId);
                selectedCourseStudentsView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to add students to the course.", e);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        Button goBackButton = new Button(I18nManager.getResourceBundle().getString("general.button.goback"));
        goBackButton.getStyleClass().add("goBackButton");

        goBackButton.setOnAction(actionEvent -> {
            try {
                SelectedCourseStudentsView selectedCourseStudentsView = new SelectedCourseStudentsView(primaryStage, courseId);
                selectedCourseStudentsView.openView();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error while trying to go back a page.", e);
            }
        });

        headerRow.getChildren().addAll(goBackButton, spacer, addStudentsButton);

        // VBox for the course students:
        VBox courseStudentsBox = new VBox(20);
        courseStudentsBox.getStyleClass().add("courseStudentsBox");
        HBox.setHgrow(courseStudentsBox, Priority.ALWAYS);
        courseStudentsBox.setMaxWidth(Double.MAX_VALUE);

        Label addStudentsLabel = new Label(I18nManager.getResourceBundle().getString("addstudents.title").toUpperCase());
        addStudentsLabel.getStyleClass().add("addStudentsLabel");
        Label instructions = new Label(I18nManager.getResourceBundle().getString("addstudents.instructions"));
        instructions.getStyleClass().add("instructions");

        VBox listAndSearchContainer = new VBox();

        HBox searchBar = new HBox(10);
        searchBar.getStyleClass().add("searchBar");
        Label searchLabel = new Label(I18nManager.getResourceBundle().getString("addstudents.label.search"));
        searchLabel.getStyleClass().add("searchLabel");
        TextField searchField = new TextField();
        searchField.getStyleClass().add("searchField");
        searchField.setPromptText(I18nManager.getResourceBundle().getString("addstudents.prompt.search"));

        // Filter students when letters are typed:
        searchField.textProperty().addListener(
                (observable, oldValue, newValue)
                        -> addStudentsController.filterStudents(newValue));
        searchBar.getChildren().addAll(searchLabel, searchField);

        studentsList.getStyleClass().add("studentsList");
        studentsList.setSpacing(8);
        ScrollPane studentListBox = new ScrollPane(studentsList);
        studentListBox.getStyleClass().add("studentListBox");
        addStudentsController.displayAvailableStudents();

        listAndSearchContainer.getChildren().addAll(searchBar, studentListBox);

        courseStudentsBox.getChildren().addAll(addStudentsLabel, instructions, listAndSearchContainer);

        content.getChildren().addAll(headerRow, courseStudentsBox);
        center.setCenter(content);

        viewBasicLayout.setTop(topBar);
        viewBasicLayout.setLeft(leftSideBar);
        viewBasicLayout.setCenter(center);

        this.primaryStage.getScene().setRoot(viewBasicLayout);
        this.primaryStage.getScene().getStylesheets().add("/styles/addstudents_style.css");
        this.primaryStage.setTitle(I18nManager.getResourceBundle().getString("window.addstudents"));
        this.primaryStage.setMaximized(true);
        this.primaryStage.show();
    }

    /**
     * Adds a student entry to the UI list with a selectable checkbox.
     *
     * @param studentId the identifier of the student
     * @param firstname the student's firstname
     * @param lastname the student's lastname
     */
    public void addToStudentList(int studentId, String firstname, String lastname) {
        HBox studentInsert = new HBox();
        studentInsert.getStyleClass().add("studentItem");

        HBox studentInfo = new HBox(20);

        Label studentIdLabel = new Label("ID " + studentId);
        studentIdLabel.getStyleClass().add("studentIdLabel");

        HBox nameBox = new HBox(5);
        nameBox.getStyleClass().add("nameBox");
        String separator = I18nManager.getCurrentLocale().getLanguage().equals("ja") ? "・" : " ";
        Label nameLabel = new Label(firstname + separator + lastname);
        nameLabel.getStyleClass().add("nameLabel");
        nameBox.getChildren().addAll(nameLabel);

        studentInfo.getChildren().addAll(studentIdLabel, nameBox);

        CheckBox selectStudentCheckBox = new CheckBox();
        selectStudentCheckBox.getStyleClass().add("selectStudentCheckBox");
        if (selectedStudentIds.contains(studentId)) {
            selectStudentCheckBox.setSelected(true);
        }

        selectStudentCheckBox.setOnAction(actionEvent -> {
            // Add or remove student's ID from the list
            try {
                if (selectStudentCheckBox.isSelected()) {
                    selectedStudentIds.add(studentId);
                } else {
                    selectedStudentIds.remove(studentId);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error while trying to edit selected students.", e);
            }
        });

        studentInsert.getChildren().addAll(studentInfo, selectStudentCheckBox);
        HBox.setHgrow(studentInfo, Priority.ALWAYS);
        studentInfo.setMaxWidth(Double.MAX_VALUE);

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
     * Clears all student entries from the UI list. Used when filtering the students.
     */
    public void clearStudentsList() {
        studentsList.getChildren().clear();
    }
}
