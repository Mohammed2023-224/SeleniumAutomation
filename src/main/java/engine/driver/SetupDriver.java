package engine.driver;

import engine.constants.Constants;
import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;

public class SetupDriver {

    public WebDriver driver;

    public WebDriver startDriver(String browser) {
        switch(browser.toLowerCase()){
            case "edge":
                driver = new Edge().initiateDriver();
                break;
            case "chrome":
                driver = new Chrome().initiateDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        return driver;
    }

    public WebDriver startDriverRemotely(String browser) {
        String proxyURL= Constants.proxyURL;
        switch(browser.toLowerCase()){
            case "edge":
                driver = new Edge().initiateRemoteDriver(proxyURL);
                break;
            case "chrome":
                driver = new Chrome().initiateRemoteDriver(proxyURL);
                break;
            default:
                throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        }
        return driver;
    }
}
