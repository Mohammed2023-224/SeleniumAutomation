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
    private int currentRetry = 0;
    private static final int maxRetry = 2;  // Hardcode or read from Constants

    @Override
    public boolean retry(ITestResult result) {
        if (currentRetry < maxRetry) {
            currentRetry++;
            Loggers.log.info("Retry attempt: " + currentRetry);
            return true;
        }
        return false;
    }
}

