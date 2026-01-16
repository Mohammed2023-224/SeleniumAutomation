package tests.baseTest;

import engine.actions.JSActions;
import engine.constants.FrameworkConfigs;
import engine.driver.SetupDriver;
import engine.listeners.AllureAttachments;
import engine.listeners.TestNgListener;
import engine.listeners.TransformListener;
import engine.reporters.Loggers;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;


@Listeners({TestNgListener.class, TransformListener.class})
public class BaseTest {
    public WebDriver driver;

    @Parameters("browser")
    @BeforeClass
    protected void InitDriver(ITestContext con, @Optional String browser) {
        driver = new SetupDriver().startDriver(browser,FrameworkConfigs.localExecution());
        con.setAttribute("driver", driver);
    }

    @AfterClass
    protected void tearDriver() {
        driver.quit();
    }

    @AfterMethod
    protected void attachLogsAndScreenshot(ITestResult result) {
        if(driver ==null) return;
        if(FrameworkConfigs.perTestLog())AllureAttachments.saveTextLog(ThreadContext.get("testLogFileName") + ".log",
                FrameworkConfigs.reportsPath() + ThreadContext.get("testLogFileName") + ".log");
        if(!result.isSuccess()) AllureAttachments.saveScreensShot(driver,"test");
    }
    @AfterMethod
    protected void startNewTab() {
        if (driver==null) return;
        JSActions.executeScript(driver,"window.open();");
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        String validTab = null;
        for (String handle : handles) {
            try {
                driver.switchTo().window(handle);
                String url = driver.getCurrentUrl();
                boolean isEdgeDownloader = url.toLowerCase().contains("edge://");
                boolean isBlank = url.equals("about:blank");
                if ( isBlank) {
                    validTab = handle;
                } else if (isEdgeDownloader) {
                 Loggers.getLogger().info("edge download tab can't be closed");
                } else {
                    driver.close();
                }

            } catch (Exception e) {
             Loggers.getLogger().info("Error handling tab: " + e.getMessage());
            }
        }
        driver.switchTo().window(validTab);

    }
}