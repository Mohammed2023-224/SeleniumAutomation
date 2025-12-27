package engine.listeners;

import engine.actions.SystemMethods;
import engine.constants.FrameworkConfigs;
import engine.driver.DriverFactory;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.testng.*;


public class TestNgCucumber extends AllureListener implements ITestListener, IRetryAnalyzer,IHookable , IExecutionListener ,IAnnotationTransformer {

    int counter = 0;
    int retryLimit = FrameworkConfigs.retryCount();

    static {
        SystemMethods.deleteDirectory("reports");
        SystemMethods.deleteDirectory("allure-results");
    }
    public void onTestStart(ITestResult result) {
    }

    public void onTestSuccess(ITestResult result) {

    }

    public void onTestFailure(ITestResult result) {

    }

    public void onTestSkipped(ITestResult result) {

    }

    public void onStart(ITestContext context) {
    }

    public void onFinish(ITestContext context) {
        ListenerHelper.stopAppenderRootLog("PerTestRouting");
     Loggers.log.info("finished Execution");
    }

    @Override
    public boolean retry(ITestResult iTestResult) {
        WebDriver mainDriver = DriverFactory.getDriver();
        if (counter < retryLimit) {
            counter++;
            mainDriver.manage().deleteAllCookies();
         Loggers.log.info("ended retry number: {}", counter);
            return true;
        }
        return false;
    }

    public void onExecutionFinish() {

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

