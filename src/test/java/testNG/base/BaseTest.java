package testNG.base;



import engine.actions.Helpers;
import engine.constants.Constants;
import engine.driver.SetupDriver;
import engine.listeners.AllureListener;
import engine.listeners.TestNg;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


@Listeners(TestNg.class)
public class BaseTest {
    public WebDriver driver;

    @Parameters("browser")
    @BeforeClass
    protected void InitDriver(ITestContext con, @Optional String browser) {
        browser = browser == null || browser.isEmpty() ? Constants.browser : browser;

        if (Constants.executionType.equalsIgnoreCase("local")) {
            driver = new SetupDriver().startDriver(browser);
        } else if (Constants.executionType.equalsIgnoreCase("remote")) {
            driver = new SetupDriver().startDriverRemotely(browser);
        }
        con.setAttribute("driver", driver);
    }

    @AfterClass
    protected void tearDriver() {
        driver.quit();
    }

    @AfterMethod
    protected void attachLogs() {
        AllureListener.saveTextLog(System.getProperty("testLogFileName") + ".log",
                Constants.reportsPath + System.getProperty("testLogFileName") + ".log");
    }
    @AfterMethod
    protected void startNewTab() {
        Helpers.initiateJSExecutor(driver).executeScript("window.open();");
        String currentHandle = driver.getWindowHandle();
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        System.out.println(handles);
        String validTab = null;
        for (String handle : handles) {
            try {
                driver.switchTo().window(handle);
//                String title = driver.getTitle();
                String url = driver.getCurrentUrl();

                boolean isEdgeDownloader = url.toLowerCase().contains("edge://");
                boolean isBlank = url.equals("about:blank");
                if ( isBlank) {
                    validTab = handle;
                } else if (isEdgeDownloader) {
                 Loggers.log.info("edge download tab can't be closed");
                } else {
                    driver.close();
                }

            } catch (Exception e) {
             Loggers.log.info("Error handling tab: " + e.getMessage());
            }
        }
        driver.switchTo().window(validTab);

    }
}