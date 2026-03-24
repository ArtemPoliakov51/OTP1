package view;

import i18n.I18nManager;
import i18n.SupportedLocale;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class LanguageSelectorView {

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
            selectLangBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    try {
                        I18nManager.setLocale(new Locale(locale.getLangAbbreviation(), locale.getCountryAbbreviation()));
                        selectionStage.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
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
