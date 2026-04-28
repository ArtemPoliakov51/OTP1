package view;

import service.I18nManager;
import service.SupportedLocale;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaFX view for displaying the language selection window.
 * <p>
 * This class creates a modal dialog where the user can choose the application's
 * language from a list of supported locales provided by {@link I18nManager}.
 * When a language is selected, the application's locale is updated and
 * the window is closed.
 * </p>
 */
public class LanguageSelectorView {

    /**
     * Logger used for recording warnings
     * and unexpected errors occurring within the view class.
     *
     * <p>This logger replaces direct stack trace printing and enables
     * structured, configurable logging suitable for production use.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(LanguageSelectorView.class.getName());

    /**
     * Private constructor for modal window.
     */
    private LanguageSelectorView() {}

    /**
     * Opens the language selection window.
     *
     * <p>
     * Each language is represented as a button. When a button is clicked:
     * <ul>
     *     <li>The application's {@link Locale} is updated via {@link I18nManager}</li>
     *     <li>The selection window is closed</li>
     * </ul>
     * </p>
     *
     * <p>
     * The window is modal ({@link Modality#APPLICATION_MODAL}), meaning the user
     * must interact with it before returning to the main application.
     * </p>
     */
    public static void openLanguageSelectionWindow() {
        Stage selectionStage = new Stage();
        VBox mainContent = new VBox(10);
        mainContent.getStyleClass().add("mainContent");
        Label title = new Label(I18nManager.getResourceBundle().getString("general.label.selectLang"));
        title.getStyleClass().add("languageTitle");

        VBox languageList = new VBox(5);
        languageList.getStyleClass().add("languageList");
        ScrollPane languageListBox = new ScrollPane(languageList);
        languageListBox.getStyleClass().add("languageListBox");

        SupportedLocale[] locales = I18nManager.getAllLocales();
        for (SupportedLocale locale : locales) {
            HBox buttonBox = new HBox();
            Button selectLangBtn = new Button(locale.getName());
            selectLangBtn.getStyleClass().add("selectLangBtn");
            selectLangBtn.setOnAction(actionEvent -> {
                try {
                    I18nManager.setLocale(new Locale(locale.getLangAbbreviation(), locale.getCountryAbbreviation()));
                    selectionStage.close();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error while trying to change the language.", e);
                }
            });
            buttonBox.getChildren().add(selectLangBtn);
            HBox.setHgrow(selectLangBtn, Priority.ALWAYS);
            selectLangBtn.setMaxWidth(Double.MAX_VALUE);
            buttonBox.setMaxWidth(Double.MAX_VALUE);
            languageList.getChildren().add(buttonBox);
        }

        mainContent.getChildren().addAll(title, languageListBox);
        languageListBox.setMaxWidth(Double.MAX_VALUE);

        Scene scene = new Scene(mainContent, 400, 200);
        scene.getStylesheets().add("/styles/language_selector_style.css");
        selectionStage.setTitle(I18nManager.getResourceBundle().getString("window.language"));
        selectionStage.setScene(scene);
        selectionStage.initModality(Modality.APPLICATION_MODAL);
        selectionStage.showAndWait();
    }
}
