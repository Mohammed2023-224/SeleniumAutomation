package tests.baseClasses;

import engine.actions.BrowserActions;
import engine.actions.WaitsManager;
import engine.constants.FrameworkConfigs;
import engine.driver.DriverFactory;
import engine.driver.DriverOptions;
import engine.driver.SetupDriver;
import engine.listeners.AllureAttachments;
import engine.listeners.TestNgListener;
import engine.listeners.TransformListener;
import engine.utils.ClassPathLoading;
import engine.utils.propertyFilesHandlers.PropertyFileCreation;
import engine.utils.propertyFilesHandlers.PropertyReader;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.Objects;


@Listeners({TestNgListener.class, TransformListener.class})
public class BaseTestClass {
//    public WebDriver driver;
    public static String testDataPath= Objects.requireNonNull(ClassPathLoading.getResourceAsPath("testData/data.xlsx", false)).toString();

    @Parameters({"browser","osVersion","browserVersion","os"})
    @BeforeClass
    protected void InitDriver(ITestContext con, @Optional() String browser,
                              @Optional("Windows") String osVersion,@Optional("latest") String browserVersion
            ,@Optional("11") String os) {
            WebDriver driver = new SetupDriver().startDriver(browser, FrameworkConfigs.localExecution(), osVersion, browserVersion, os);
        DriverOptions.maximizeWindow(driver,FrameworkConfigs.maximized());
        DriverFactory.setDriver(driver);
        con.setAttribute("driver", driver);
        WaitsManager.setWaits(driver);
    }

    @AfterClass
    protected void tearDriver() {
        DriverFactory.getDriver().quit();
        WaitsManager.removeWaits();
        DriverFactory.unload();
    }

    @AfterMethod
    protected void attachLogsAndScreenshot(ITestResult result) {
        if(DriverFactory.getDriver() ==null) return;
        if(FrameworkConfigs.perTestLog())AllureAttachments.saveTextLog(ThreadContext.get("testLogFileName") + ".log",
                System.getProperty("user.dir")+"/"+FrameworkConfigs.reportsPath() + ThreadContext.get("testLogFileName") + ".log");
        if(!result.isSuccess()) AllureAttachments.saveScreensShot(DriverFactory.getDriver(),"test");
        ThreadContext.remove("testLogFileName");
    }
    @AfterMethod
    protected void startNewTab() {
        BrowserActions.startNewTab(DriverFactory.getDriver());
    }

}