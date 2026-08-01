package tests.baseClasses;

import engine.actions.WaitsManager;
import engine.constants.FrameworkConfigs;
import engine.driver.SetupDriver;
import engine.driver.androidDriver.AndroidDriverFactory;
import engine.listeners.AllureAttachments;
import engine.listeners.TestNgListener;
import engine.listeners.TransformListener;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

@Listeners({TestNgListener.class, TransformListener.class})

public class BaseMobileTestClass {
    @BeforeClass
    protected void InitDriver(ITestContext con) {
        AppiumDriver driver = new SetupDriver().startMobileDriver();
        AndroidDriverFactory.setDriver(driver);
        con.setAttribute("driver", driver);
        WaitsManager.setWaits(driver);
    }

    @AfterClass
    protected void tearDriver() {
        AndroidDriverFactory.getDriver().quit();
        WaitsManager.removeWaits();
        AndroidDriverFactory.unload();
    }

    @AfterMethod
    protected void attachLogsAndScreenshot(ITestResult result) {
        if(AndroidDriverFactory.getDriver() ==null) return;
        if(FrameworkConfigs.perTestLog()) AllureAttachments.saveTextLog(ThreadContext.get("testLogFileName") + ".log",
                System.getProperty("user.dir")+"/"+FrameworkConfigs.reportsPath() + ThreadContext.get("testLogFileName") + ".log");
        if(!result.isSuccess()) AllureAttachments.saveScreensShot(AndroidDriverFactory.getDriver(),"test");
        ThreadContext.remove("testLogFileName");
    }

    @BeforeSuite
    public void publishAllureEnvironment() {
////        String environment= PropertyReader.get("environment", String.class);
//        LinkedHashMap<String, String> env = new LinkedHashMap<>();
////        env.put("Environment", environment);
////        env.put("Base URL", EnvSelector.envSelector(false));
////        env.put("Run Type", PropertyReader.get("runType", String.class));
//        env.put("Local Execution",
//                PropertyReader.get("local_execution", String.class));
//        env.put("Java", System.getProperty("java.version"));
//
//        PropertyFileCreation.createPropertyFile(env,"allure-results","environment"
//                ,"Allure Environment");
    }
}
