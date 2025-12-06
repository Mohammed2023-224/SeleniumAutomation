package engine.listeners;

import engine.constants.Constants;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

public class RetryListener implements IRetryAnalyzer {
    int retryLimit = Constants.retryCount;

    private Map<String, Integer> retryCountMap = new HashMap<>();

    @Override
    public boolean retry(ITestResult iTestResult) {
        String testName = iTestResult.getMethod().getMethodName();
        WebDriver mainDriver = (WebDriver) iTestResult.getTestContext().getAttribute("driver");
        int counter = retryCountMap.getOrDefault(testName, 0);
        if (counter < retryLimit) {
            retryCountMap.put(testName, counter + 1);
            mainDriver.manage().deleteAllCookies();
            Loggers.log.info("Retrying test {} (retry {}/{})",
                    testName, counter + 1, Constants.retryCount);

            return true;
        }
        return false;
    }
}
