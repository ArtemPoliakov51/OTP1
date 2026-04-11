package i18n;

import java.util.*;

/**
 * Provides utility methods for managing application internationalization (i18n).
 *
 * <p>This class maintains the currently selected {@link Locale} and the associated
 * {@link ResourceBundle}, which is used to retrieve localized UI strings.</p>
 *
 * <p>The class is designed as a static utility and cannot be instantiated.</p>
 */
public class I18nManager {

    /**
     * The currently active Locale of the application.
     */
    private static Locale currentLocale;

    /**
     * The currently loaded ResourceBundle of the application.
     */
    private static ResourceBundle resourceBundle;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private I18nManager() {
    }


    /**
     * Sets the current locale and loads the corresponding resource bundle.
     *
     * @param locale the locale to be set for the application
     */
    public static void setLocale(Locale locale) {
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("UITextBundle", locale);
    }

    /**
     * Returns the currently active locale.
     *
     * @return the current locale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Returns the resource bundle associated with the current locale.
     *
     * @return the resource bundle for localized UI strings
     */
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Returns all supported locales defined in the application.
     *
     * @return an array of supported locales
     */
    public static SupportedLocale[] getAllLocales() {
        return SupportedLocale.values();
    }
}
