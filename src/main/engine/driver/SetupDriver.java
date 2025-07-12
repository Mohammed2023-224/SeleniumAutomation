package engine.driver;

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
        }
        return driver;
    }
}
