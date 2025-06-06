package engine.driver;

import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;

public class SetupDriver {

    public WebDriver driver;

    public WebDriver startDriver(String browser) {
        if (browser.equalsIgnoreCase("edge")) {
            driver = new Edge().initiateDriver();
            Loggers.log.info("test");
        }
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new Chrome().initiateDriver();
        }
        return driver;
    }
}
