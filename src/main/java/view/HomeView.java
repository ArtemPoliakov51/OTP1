package view;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class HomeView {

    private BorderPane layout;

    protected HomeView() {
        BorderPane mainLayout = new BorderPane();

        VBox topBar = new VBox();
        Label topBarLabel = new Label("ATTENDANCE CHECKER");
        topBar.getChildren().add(topBarLabel);

        mainLayout.setTop(topBar);

        this.layout = mainLayout;
    }

    public BorderPane getHomeView() {
        return this.layout;
    }
}
