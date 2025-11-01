package cucumber.stepDefinitions;

import engine.constants.Constants;
import engine.driver.SetupDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    public WebDriver driver;

    @Before
    public void InitDriver() {
        String browser = !Constants.browser.isEmpty() ? Constants.browser : "edge";
        if (Constants.executionType.equalsIgnoreCase("local")) {
            driver = new SetupDriver().startDriver(browser);
        } else if (Constants.executionType.equalsIgnoreCase("remote")) {
            driver = new SetupDriver().startDriverRemotely(browser);
        }
        DriverFactory.setDriver(driver);
    }

    @After
    public void tearDriver() {
        DriverFactory.getDriver().quit();
        DriverFactory.unload();
    }

}
