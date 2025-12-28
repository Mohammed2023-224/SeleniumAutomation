package testNG.base;



import engine.actions.JSActions;
import engine.constants.FrameworkConfigs;
import engine.driver.SetupDriver;
import engine.enums.Browsers;
import engine.listeners.AllureListener;
import engine.listeners.TestExecutionListener;
import engine.listeners.TransformListener;
import engine.reporters.Loggers;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Listeners({TestExecutionListener.class, TransformListener.class})
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
    protected void attachLogs(ITestResult result) {
        if(driver ==null) return;
        AllureListener.saveTextLog(ThreadContext.get("testLogFileName") + ".log",
                FrameworkConfigs.reportsPath() + ThreadContext.get("testLogFileName") + ".log");
        if(!result.isSuccess()) AllureListener.saveScreensShot(driver,"test");
    }
    @AfterMethod
    protected void startNewTab() {
        if (driver==null) return;
        JSActions.executeScript(driver,"window.open();");
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        System.out.println(handles);
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