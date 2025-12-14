package engine.listeners;

import engine.actions.SystemMethods;
import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.GmailHandler;
import engine.utils.ReadExecutionFlow;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class TestExecutionListener extends AllureListener implements ITestListener , IExecutionListener  {


    private static final List<String> successfulTests = Collections.synchronizedList(new ArrayList<>());
    private static final List<String> failedTests = Collections.synchronizedList(new ArrayList<>());
    private static final List<String> skippedTests = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicInteger numberOfSuccessTest = new AtomicInteger(0);
    private static final AtomicInteger numberOfFailedTests = new AtomicInteger(0);
    private static final AtomicInteger numberOfSkippedTests = new AtomicInteger(0);
    static {
        SystemMethods.deleteDirectory("reports");
        SystemMethods.deleteDirectory("allure-results");
    }
    @Override
    public void onTestStart(ITestResult result) {
        String browserName = getBrowserName((WebDriver) result.getTestContext().getAttribute("driver")); // You'll define this method
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
        String name = result.getMethod().getMethodName();
        String fileName = name +"-"+browserName+"-"+timestamp;
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        ThreadContext.put("testLogFileName", fileName); // ✅ thread-local
//        ListenerHelper.reconfigureLogs();
     Loggers.log.info("Start test: {}", result.getName());

    }
    @Override
    public void onTestSuccess(ITestResult result) {
     Loggers.log.info("Test succeeded: {}", result.getName());
        numberOfSuccessTest.incrementAndGet();
        successfulTests.add(result.getName());
    }
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = (WebDriver)  result.getTestContext().getAttribute("driver");
        saveScreensShot(driver, "failed test screenshot");
     Loggers.log.info("Test failed: {}", result.getName());
        numberOfFailedTests.incrementAndGet();
        failedTests.add(result.getName());
//        saveScreensShot(driver, "failed test screenshot");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
     Loggers.log.info("Test skipped: {}", result.getName());
        numberOfSkippedTests.incrementAndGet();
        skippedTests.add(result.getName());
    }
    @Override
    public void onStart(ITestContext context) {
    }
    @Override
    public void onFinish(ITestContext context) {
     Loggers.log.info("finished Execution");
    }
    @Override
    public void onExecutionFinish() {
     Loggers.log.info("Number of all tests: {}", (numberOfSuccessTest.get()+numberOfFailedTests.get()+numberOfSkippedTests.get()));
     Loggers.log.info("Number of successful tests: {}", numberOfSuccessTest.get());
     Loggers.log.info("Name of successful tests: {}", Arrays.deepToString(successfulTests.toArray()));
     Loggers.log.info("Number of failed tests: {}", numberOfFailedTests.get());
     Loggers.log.info("Name of failed tests: {}", Arrays.deepToString(failedTests.toArray()));
     Loggers.log.info("Number of skipped tests: {}", numberOfSkippedTests.get());
     Loggers.log.info("Name of skipped tests: {}", Arrays.deepToString(skippedTests.toArray()));
        if(Constants.generateAndSendReport.equalsIgnoreCase("true")){
            SystemMethods.runFile(Constants.generateAllureReport);
            GmailHandler gmailHandler=new GmailHandler("test");
            gmailHandler.sendEmail(Constants.emailRecipient,Constants.emailCopied,Constants.emailSubject
                    ,Constants.emailBody,Constants.emailAttachmentPath);
        }
        if (Constants.openAllure.equalsIgnoreCase("true")) {
         Loggers.log.info("start allure report pls don't stop the execution");
            SystemMethods.runFile(Constants.allureFile);
        }
    ListenerHelper.stopAppenderRootLog("PerTestRouting");
        SystemMethods.deleteFile("reports/log4j/perTest/${ctx");
        if (Constants.seleniumGridRun.equalsIgnoreCase("true")) {
            System.out.println("Test execution finished. Cleaning up Selenium Grid...");
            SystemMethods.killProcessesByPort(4444,5555);
            System.out.println("Cleanup completed.");
        }
    }
    @Override
    public void onExecutionStart() {
        if(Constants.seleniumGridRun.equalsIgnoreCase("true")){
            SystemMethods.startBatAsync("src/main/resources/grid/startHub.bat");
            SystemMethods.startBatAsync("src/main/resources/grid/startNode.bat");
        }
    }


    public static String getBrowserName(WebDriver driver) {
        if (driver instanceof RemoteWebDriver) {
            return ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase();
        }
        return "unknown";
    }

}

