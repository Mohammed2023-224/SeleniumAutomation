package engine.listeners;

import engine.actions.SystemMethods;
import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;
import engine.utils.GmailHandler;
import engine.utils.PropertyReader;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.testng.annotations.BeforeMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class TestNgListener implements ITestListener , IExecutionListener ,IInvokedMethodListener {


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
        String browserName = getBrowserName((WebDriver) result.getTestContext().getAttribute("driver"));
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
        String name = result.getMethod().getMethodName();
        String fileName = name +"-"+browserName+"-"+timestamp;
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");
        ThreadContext.put("testLogFileName", fileName); // ✅ thread-local
//        ListenerHelper.reconfigureLogs();
     Loggers.getLogger().info("Start test: {}", result.getName());

    }
    @Override
    public void onTestSuccess(ITestResult result) {
     Loggers.getLogger().info("Test succeeded: {}", result.getName());
        numberOfSuccessTest.incrementAndGet();
        successfulTests.add(result.getName());
    }
    @Override
    public void onTestFailure(ITestResult result) {
     Loggers.getLogger().info("Test failed: {}", result.getName());
        numberOfFailedTests.incrementAndGet();
        failedTests.add(result.getName());

    }

    @Override
    public void onTestSkipped(ITestResult result) {
     Loggers.getLogger().info("Test skipped: {}", result.getName());
        numberOfSkippedTests.incrementAndGet();
        skippedTests.add(result.getName());
    }
    @Override
    public void onStart(ITestContext context) {
    }
    @Override
    public void onFinish(ITestContext context) {
     Loggers.getLogger().info("finished Execution");
    }
    @Override
    public void onExecutionFinish() {
     Loggers.getLogger().info("Number of all tests: {}", (numberOfSuccessTest.get()+numberOfFailedTests.get()+numberOfSkippedTests.get()));
     Loggers.getLogger().info("Number of successful tests: {}", numberOfSuccessTest.get());
     Loggers.getLogger().info("Name of successful tests: {}", Arrays.deepToString(successfulTests.toArray()));
     Loggers.getLogger().info("Number of failed tests: {}", numberOfFailedTests.get());
     Loggers.getLogger().info("Name of failed tests: {}", Arrays.deepToString(failedTests.toArray()));
     Loggers.getLogger().info("Number of skipped tests: {}", numberOfSkippedTests.get());
     Loggers.getLogger().info("Name of skipped tests: {}", Arrays.deepToString(skippedTests.toArray()));
        if(FrameworkConfigs.sendReportEmail()){
            SystemMethods.runFile(FrameworkConfigs.allureGenerationPath());
            GmailHandler gmailHandler=new GmailHandler("test");
            gmailHandler.sendEmail(FrameworkConfigs.emailTo(), FrameworkConfigs.emailCc(), FrameworkConfigs.emailSubject()
                    , FrameworkConfigs.emailBody(), FrameworkConfigs.emailAttachmentPath());
        }
        if (FrameworkConfigs.openAllure()) {
         Loggers.getLogger().info("start allure report pls don't stop the execution");
            SystemMethods.runFile(FrameworkConfigs.allureGenerationPath());
        }
        if (PropertyReader.get("kill_processes", Boolean.class)) {
            Loggers.getLogger().info("Test execution finished. cleaning up proccesses...");
            String portsValue = PropertyReader.get("portsToCloseBeforeFinishingExecution", String.class);
            int[] ports = Arrays.stream(portsValue.split(","))
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            SystemMethods.killProcessesByPort(ports);
            Loggers.getLogger().info("Cleanup completed.");
        }
    }
    @Override
    public void onExecutionStart() {

        if(FrameworkConfigs.gridEnabled()){
            SystemMethods.startBatAsync(FrameworkConfigs.gridPath()+"startHub.bat");
            SystemMethods.startBatAsync(FrameworkConfigs.gridPath()+"startNode.bat");
        }
    }


    public static String getBrowserName(WebDriver driver) {
        if (driver instanceof RemoteWebDriver) {
            return ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase();
        }
        return "unknown";
    }


}

