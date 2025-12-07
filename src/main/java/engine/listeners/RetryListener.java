package engine.listeners;

import engine.constants.Constants;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RetryListener implements IRetryAnalyzer {
    private final int retryLimit = Constants.retryCount;
    private final Map<String, Integer> retryCountMap = new ConcurrentHashMap<>();

    public boolean retry(ITestResult result) {
        try {
            String key = result.getTestClass().getName() + "." + result.getMethod().getMethodName();
            int count = retryCountMap.getOrDefault(key, 0);

            if (count < retryLimit) {
                retryCountMap.put(key, count + 1);
                Loggers.log.info("Retrying test {} ({}/{})", key, count + 1, retryLimit);
                return true;
            }

        } catch (Exception e) {
            Loggers.log.error("Retry failed internally: " + e.getMessage());
        }
        return false;
    }
}
