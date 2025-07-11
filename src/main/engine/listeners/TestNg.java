package engine.listeners;

import engine.actions.SystemMethods;
import engine.constants.Constants;
import engine.reporters.Loggers;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;


public class TestNg extends AllureListener implements ITestListener, IRetryAnalyzer,IHookable , IExecutionListener {
    int numberOfFailedTests = 0;
    int numberOfSuccessTest = 0;
    int numberOfSkippedTests = 0;
    int counter=0;
    int retryLimit= 3;
    ArrayList<String> successfulTests = new ArrayList<>();
    ArrayList<String> failedTests = new ArrayList<>();
    ArrayList<String> skippedTests = new ArrayList<>();

    public void onTestStart(ITestResult result) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        String name = result.getMethod().getMethodName();
        String fileName = name + "-" + timestamp;
        // Clean up for Windows (remove illegal characters)
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        // Set system property for this test
        System.setProperty("testLogFileName", fileName);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
        Loggers.log.info("Start test: {}", result.getName());
    }


    public void onTestSuccess(ITestResult result) {
        Loggers.log.info("Test succeeded: {}", result.getName());
        numberOfSuccessTest++;
        successfulTests.add(result.getName());
    }

    public void onTestFailure(ITestResult result ,ITestContext con) {
        WebDriver driver = (WebDriver) con.getAttribute("driver");
        Loggers.log.info("Test failed: {}", result.getName());
        numberOfFailedTests++;
        failedTests.add(result.getName());
        saveScreensShot(driver, "failed test screenshot");

    }

    public void onTestSkipped(ITestResult result) {
        Loggers.log.info("Test skipped: {}", result.getName());
        numberOfSkippedTests++;
        skippedTests.add(result.getName());
    }


    public void onStart(ITestContext context) {
        SystemMethods.deleteDirectory("reports");
        SystemMethods.deleteDirectory("allure-results");
    }

    public void onFinish(ITestContext context) {
        Loggers.log.info("finished Execution");

    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        WebDriver mainDriver = (WebDriver) iTestResult.getTestContext().getAttribute("driver");
        if (counter <= retryLimit) {
            counter++;
            mainDriver.manage().deleteAllCookies();
            Loggers.log.info("ended retry number: {}", counter);
            return true;
        }
        return false;
    }

    public void onExecutionFinish() {
        Loggers.log.info("Number of all tests: {}", numberOfSuccessTest + numberOfFailedTests + numberOfSkippedTests);
        Loggers.log.info("Number of successful tests: {}", numberOfSuccessTest);
        Loggers.log.info("Name of successful tests: {}", Arrays.deepToString(successfulTests.toArray()));
        Loggers.log.info("Number of failed tests: {}", numberOfFailedTests);
        Loggers.log.info("Name of failed tests: {}", Arrays.deepToString(failedTests.toArray()));
        Loggers.log.info("Number of skipped tests: {}", numberOfSkippedTests);
        Loggers.log.info("Name of skipped tests: {}", Arrays.deepToString(skippedTests.toArray()));
        if (Constants.openAllure.equalsIgnoreCase("true")) {
            Loggers.log.info("start allure report pls don't stop the execution");
            SystemMethods.runFile(Constants.allureFile);
        }
    }

    public void onExecutionStart() {

    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);
        if (testResult.getThrowable() != null) {
            if (retry(testResult)) {
                callBack.runTestMethod(testResult);
            }
        }
    }
}
