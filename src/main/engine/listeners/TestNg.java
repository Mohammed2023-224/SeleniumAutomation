package engine.listeners;

import engine.actions.SystemMethods;
import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.ReadExecutionFlow;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class TestNg extends AllureListener implements ITestListener, IRetryAnalyzer,IHookable , IExecutionListener ,IAnnotationTransformer {
    int numberOfFailedTests = 0;
    int numberOfSuccessTest = 0;
    int numberOfSkippedTests = 0;
    int counter = 0;
    int retryLimit = 0;
    ArrayList<String> successfulTests = new ArrayList<>();
    ArrayList<String> failedTests = new ArrayList<>();
    ArrayList<String> skippedTests = new ArrayList<>();
    Set<String> runningTests = new ReadExecutionFlow().readExecutionControl();
    static {
        SystemMethods.deleteDirectory("reports");
        SystemMethods.deleteDirectory("allure-results");
    }
    public void onTestStart(ITestResult result) {
        String browserName = getBrowserName((WebDriver) result.getTestContext().getAttribute("driver")); // You'll define this method
        System.setProperty("testLogFileName", browserName + "_" + result.getMethod().getMethodName());
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
        String name = result.getMethod().getMethodName();
        String fileName = name +"-"+browserName+"-"+timestamp;
        // Clean up for Windows (remove illegal characters)
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        // Set system property for this test
        System.setProperty("testLogFileName", fileName);

        ListenerHelper.reconfigureLogs();
        Loggers.getInstance().log.info("Start test: {}", result.getName());
    }

    public void onTestSuccess(ITestResult result) {
        Loggers.getInstance().log.info("Test succeeded: {}", result.getName());
        numberOfSuccessTest++;
        successfulTests.add(result.getName());
    }

    public void onTestFailure(ITestResult result, ITestContext con) {
        WebDriver driver = (WebDriver) con.getAttribute("driver");
        Loggers.getInstance().log.info("Test failed: {}", result.getName());
        numberOfFailedTests++;
        failedTests.add(result.getName());
        saveScreensShot(driver, "failed test screenshot");
    }

    public void onTestSkipped(ITestResult result) {
        Loggers.getInstance().log.info("Test skipped: {}", result.getName());
        numberOfSkippedTests++;
        skippedTests.add(result.getName());
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
        ListenerHelper.stopAppenderRootLog("PerTestRouting");
        Loggers.getInstance().log.info("finished Execution");
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        WebDriver mainDriver = (WebDriver) iTestResult.getTestContext().getAttribute("driver");
        if (counter < retryLimit) {
            counter++;
            mainDriver.manage().deleteAllCookies();
            Loggers.getInstance().log.info("ended retry number: {}", counter);
            return true;
        }
        return false;
    }

    public void onExecutionFinish() {
        Loggers.getInstance().log.info("Number of all tests: {}", numberOfSuccessTest + numberOfFailedTests + numberOfSkippedTests);
        Loggers.getInstance().log.info("Number of successful tests: {}", numberOfSuccessTest);
        Loggers.getInstance().log.info("Name of successful tests: {}", Arrays.deepToString(successfulTests.toArray()));
        Loggers.getInstance().log.info("Number of failed tests: {}", numberOfFailedTests);
        Loggers.getInstance().log.info("Name of failed tests: {}", Arrays.deepToString(failedTests.toArray()));
        Loggers.getInstance().log.info("Number of skipped tests: {}", numberOfSkippedTests);
        Loggers.getInstance().log.info("Name of skipped tests: {}", Arrays.deepToString(skippedTests.toArray()));
        if (Constants.openAllure.equalsIgnoreCase("true")) {
            Loggers.getInstance().log.info("start allure report pls don't stop the execution");
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

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod == null) return; // Don't process config methods or null test methods
        String className = testClass != null ? testClass.getSimpleName() : testMethod.getDeclaringClass().getSimpleName();
        String testSignature = (className + "." + testMethod.getName()).trim().toLowerCase();
        if (!runningTests.contains(testSignature.toLowerCase())) {
            annotation.setEnabled(false);
            Loggers.getInstance().log.info("⛔ Skipping: " + testSignature);
        } else {
            Loggers.getInstance().log.info("✅ Executing: " + testSignature);
        }
    }

    public static String getBrowserName(WebDriver driver) {
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        return caps.getBrowserName().toLowerCase(); // chrome, edge, firefox
    }
}

