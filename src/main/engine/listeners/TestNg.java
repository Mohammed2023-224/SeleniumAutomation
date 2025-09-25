package engine.listeners;

import engine.actions.SystemMethods;
import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.GmailHandler;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class TestNg extends AllureListener implements ITestListener, IRetryAnalyzer,IHookable , IExecutionListener ,IAnnotationTransformer {

    int counter = 0;
    int retryLimit = Constants.retryCount;
    Set<String> runningTests = new ReadExecutionFlow().readExecutionControl();

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
    public void onTestStart(ITestResult result) {
        String browserName = getBrowserName((WebDriver) result.getTestContext().getAttribute("driver")); // You'll define this method
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
        String name = result.getMethod().getMethodName();
        String fileName = name +"-"+browserName+"-"+timestamp;
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        System.setProperty("testLogFileName", fileName);
        ListenerHelper.reconfigureLogs();
     Loggers.log.info("Start test: {}", result.getName());
    }

    public void onTestSuccess(ITestResult result) {
     Loggers.log.info("Test succeeded: {}", result.getName());
        numberOfSuccessTest.incrementAndGet();
        successfulTests.add(result.getName());
    }

    public void onTestFailure(ITestResult result, ITestContext con) {
        WebDriver driver = (WebDriver) con.getAttribute("driver");
     Loggers.log.info("Test failed: {}", result.getName());
        numberOfFailedTests.incrementAndGet();
        failedTests.add(result.getName());
        saveScreensShot(driver, "failed test screenshot");
    }

    public void onTestSkipped(ITestResult result) {
     Loggers.log.info("Test skipped: {}", result.getName());
        numberOfSkippedTests.incrementAndGet();
        skippedTests.add(result.getName());
    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
        ListenerHelper.stopAppenderRootLog("PerTestRouting");
     Loggers.log.info("finished Execution");
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        WebDriver mainDriver = (WebDriver) iTestResult.getTestContext().getAttribute("driver");
        if (counter < retryLimit) {
            counter++;
            mainDriver.manage().deleteAllCookies();
         Loggers.log.info("ended retry number: {}", counter);
            return true;
        }
        return false;
    }

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
            gmailHandler.sendEmail("shadowmoon57199@gmail.com","","etst","test","allure-report/index.html");
        }
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

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (testMethod == null) return; // Don't process config methods or null test methods
        String className = testClass != null ? testClass.getSimpleName() : testMethod.getDeclaringClass().getSimpleName();
        String testSignature = (className + "." + testMethod.getName()).trim().toLowerCase();
        if (!runningTests.contains(testSignature.toLowerCase())) {
            annotation.setEnabled(false);
         Loggers.log.info("⛔ Skipping: " + testSignature);
        } else {
         Loggers.log.info("✅ Executing: " + testSignature);
        }
    }

    public static String getBrowserName(WebDriver driver) {
        Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
        return caps.getBrowserName().toLowerCase(); // chrome, edge, firefox
    }
}

