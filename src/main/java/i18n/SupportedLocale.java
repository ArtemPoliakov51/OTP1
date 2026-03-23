package i18n;

public enum SupportedLocale {
    EN("English", "en", "US"),
    FI("Suomi", "fi", "FI");

    private String name;
    private String langAbbreviation;
    private String countryAbbreviation;

    SupportedLocale(String name, String lang, String country) {
        this.name = name;
        this.langAbbreviation = lang;
        this.countryAbbreviation = country;
    }

    public String getName() {
        return name;
    }

    public String getLangAbbreviation() {
        return langAbbreviation;
    }

    public String getCountryAbbreviation() {
        return countryAbbreviation;
    }
}
