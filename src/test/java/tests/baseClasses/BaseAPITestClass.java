package tests.baseClasses;

import engine.assertions.SoftAssertManager;
import engine.constants.FrameworkConfigs;
import engine.listeners.AllureAttachments;
import engine.listeners.TestNgListener;
import engine.listeners.TransformListener;
import engine.utils.ClassPathLoading;
import engine.utils.propertyFilesHandlers.PropertyFileCreation;
import engine.utils.propertyFilesHandlers.PropertyReader;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.LinkedHashMap;
import java.util.Objects;

@Listeners({TestNgListener.class, TransformListener.class})
public class BaseAPITestClass {
    public String testDataPath= Objects.requireNonNull(ClassPathLoading.getResourceAsPath("testData/data.xlsx", false)).toString();

    @BeforeMethod
    protected void initSoftBooking() {
        SoftAssertManager.init();
    }

    @AfterMethod
    protected void attachLogsAndScreenshot(ITestResult result) {
        if(FrameworkConfigs.perTestLog())AllureAttachments.saveTextLog(ThreadContext.get("testLogFileName") + ".log",
                System.getProperty("user.dir")+"/"+FrameworkConfigs.reportsPath() + ThreadContext.get("testLogFileName") + ".log");
    }
}