import seed_data.SeedDataInserter;
import view.LoginView;

public class Main {
    public static void main(String[] args) {
        SeedDataInserter.runIfNeeded();
        LoginView.launch(LoginView.class);
    }
}
