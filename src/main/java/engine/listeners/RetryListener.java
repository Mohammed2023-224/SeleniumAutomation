package engine.listeners;

import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {
        private int retryCount = 0;    int maxRetry= FrameworkConfigs.retryCount();
        @Override
        public boolean retry(ITestResult result) {
            int current = retryCount;
            if (current < maxRetry) {
                retryCount++;
                Loggers.logInfo(
                        "Retry " + (current + 1) + " for " + result.getName());
                return true;
            }
            return false;
        }
}