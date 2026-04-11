import javafx.application.Application;
import seed_data.SeedDataInserter;
import view.LoginView;

/**
 * Entry point of the application.
 *
 * <p>This class initializes required data and launches the user interface.</p>
 *
 * <p>On startup, it ensures that seed data is inserted into the database if needed,
 * and then starts the application by launching the {@link LoginView}.</p>
 */
public class Main {
    public static void main(String[] args) {
        SeedDataInserter.runIfNeeded();
        Application.launch(LoginView.class);
    }
}
