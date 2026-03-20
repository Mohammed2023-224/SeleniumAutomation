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
            Loggers.logInfo("Retry "+current + 1+" for "+ result.getName());
            return true;
        }
        retryCount.remove();
        return false;
    }
}