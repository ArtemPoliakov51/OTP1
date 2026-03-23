package i18n;

import java.util.*;

/**
 * Manager class for managing the internationalization of the UI
 */
public class I18nManager {

    private static Locale currentLocale;
    private static ResourceBundle resourceBundle;

    /**
     * Private constructor of the class
     */
    private I18nManager() {
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("UITextBundle", locale);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static SupportedLocale[] getAllLocales() {
        return SupportedLocale.values();
    }
}
