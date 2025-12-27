package engine.listeners;

import engine.reporters.Loggers;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

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

