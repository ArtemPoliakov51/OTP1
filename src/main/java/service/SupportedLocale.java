package service;

/**
 * Enumeration of supported locales in the application.
 *
 * <p>Each constant defines a language supported by the system, including:
 * <ul>
 *   <li>Display name of the language</li>
 *   <li>ISO language code</li>
 *   <li>ISO country code</li>
 * </ul>
 *
 * <p>This enum is used for selecting and managing available UI languages.</p>
 */
public enum SupportedLocale {
    EN("English", "en", "US"),
    FI("Suomi", "fi", "FI"),
    JA("日本語", "ja", "JP"),
    EL("Ελληνική", "el", "GR");

    /**
     * Display name of the language.
     */
    private final String name;

    /**
     * ISO language code (e.g., "en", "fi").
     */
    private final String langAbbreviation;

    /**
     * ISO country code (e.g., "US", "FI").
     */
    private final String countryAbbreviation;

    /**
     * Constructs a SupportedLocale with language and country information.
     *
     * @param name display name of the language
     * @param lang ISO language code
     * @param country ISO country code
     */
    SupportedLocale(String name, String lang, String country) {
        this.name = name;
        this.langAbbreviation = lang;
        this.countryAbbreviation = country;
    }

    /**
     * Returns the display name of the language.
     *
     * @return the language name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ISO language code.
     *
     * @return the language abbreviation (e.g., "en")
     */
    public String getLangAbbreviation() {
        return langAbbreviation;
    }

    /**
     * Returns the ISO country code.
     *
     * @return the country abbreviation (e.g., "US")
     */
    public String getCountryAbbreviation() {
        return countryAbbreviation;
    }
}
