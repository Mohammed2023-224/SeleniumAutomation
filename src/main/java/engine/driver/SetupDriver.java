package engine.driver;

import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import org.openqa.selenium.WebDriver;

public class SetupDriver {

//    public WebDriver driver;

    public  WebDriver startDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "edge" -> new Edge().initiateDriver();
            case "chrome" -> new Chrome().initiateDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    public WebDriver startDriverRemotely(String browser,String proxy) {
        return switch (browser.toLowerCase()) {
            case "edge" -> new Edge().initiateRemoteDriver(proxy);
            case "chrome" -> new Chrome().initiateRemoteDriver(proxy);
            default -> throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        };
    }
}
