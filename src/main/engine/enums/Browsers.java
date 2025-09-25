package engine.enums;

public enum Browsers {
    EDGE ("edge"),
    CHROME ("chrome"),
    FIREFOX ("firefox");

    private final String browser;
    public String getMethod() {
        return browser;
    }
    Browsers(String browser) {
        this.browser=browser;
    }

}
