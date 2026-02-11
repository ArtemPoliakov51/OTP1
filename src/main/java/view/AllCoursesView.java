package view;

import controller.AllCoursesController;
import controller.LoginController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class AllCoursesView {

    private BorderPane viewBasicLayout;
    private AllCoursesController allCoursesController;
    private VBox coursesList = new VBox();

    protected AllCoursesView(LoginController loginController) {
        this.allCoursesController = new AllCoursesController(this, loginController);

        viewBasicLayout = new BorderPane();
        viewBasicLayout.setCenter(coursesList);

        allCoursesController.displayActiveCourses();
    }

    public void addToActiveCoursesList(String courseIdentifier, String courseName, LocalDateTime created, int courseId) {
        HBox courseInsert = new HBox();
        Button courseSelector = new Button();

        HBox courseInfo = new HBox();
        Label cIdentifier = new Label(courseIdentifier);
        Label cName = new Label(courseName);
        Label cDate = new Label(created.toString());

        courseInfo.getChildren().addAll(cIdentifier, cName, cDate);
        courseSelector.setGraphic(courseInfo);

        Button archiveCourseButton = new Button("ARCHIVE");

        courseInsert.getChildren().addAll(courseSelector, archiveCourseButton);
        coursesList.getChildren().add(courseInsert);
    }

    public BorderPane getCoursesContent() {
        return this.viewBasicLayout;
    }
}
