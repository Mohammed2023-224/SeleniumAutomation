package engine.listeners;

import engine.reporters.Loggers;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {
    private final ThreadLocal<Integer> retryCount = ThreadLocal.withInitial(() -> 0);
    int maxRetry=3;
    @Override
    public boolean retry(ITestResult result) {
        int current = retryCount.get();
        if (current < maxRetry) {
            retryCount.set(current + 1);
            Loggers.log.info("Retry {} for {}", current + 1, result.getName());
            return true;
        }
        return false;
    }
}

