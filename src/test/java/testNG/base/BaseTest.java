package testNG.base;



import engine.actions.JSActions;
import engine.constants.Constants;
import engine.driver.SetupDriver;
import engine.listeners.AllureListener;
import engine.listeners.TestExecutionListener;
import engine.listeners.TransformListener;
import engine.reporters.Loggers;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
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
        browser = browser == null || browser.isEmpty() ? Constants.browser : browser;
        String port=System.getProperty("port");
        port = port == null || port.isEmpty() ? Constants.proxyURL :port;
        if (Constants.executionType.equalsIgnoreCase("local")) {
            driver = new SetupDriver().startDriver(browser);
        } else if (Constants.executionType.equalsIgnoreCase("remote")) {
            if(Constants.seleniumGridRun.equalsIgnoreCase("true")){
                waitForGrid(port,15);
            }
            driver = new SetupDriver().startDriverRemotely(browser,port);
        }
        con.setAttribute("driver", driver);
    }
    public static void waitForGrid(String gridUrl, int timeoutSeconds) {
        long end = System.currentTimeMillis() + timeoutSeconds * 1000;
        while (System.currentTimeMillis() < end) {
            try {
                HttpURLConnection con =
                        (HttpURLConnection) new URL(gridUrl + "/status").openConnection();
                con.setConnectTimeout(1000);
                if (con.getResponseCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {}
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        throw new RuntimeException("Grid not ready");
    }
    @AfterClass
    protected void tearDriver() {
        driver.quit();
    }

    @AfterMethod
    protected void attachLogs() {
        AllureListener.saveTextLog(ThreadContext.get("testLogFileName") + ".log",
                Constants.reportsPath + ThreadContext.get("testLogFileName") + ".log");
        AllureListener.saveScreensShot(driver,"test");
    }
    @AfterMethod
    protected void startNewTab() {
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