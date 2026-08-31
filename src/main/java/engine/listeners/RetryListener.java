package engine.listeners;

import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
 public class RetryListener implements IRetryAnalyzer {
        private int retryCount = 0;
        private final int maxRetry = FrameworkConfigs.retryCount();
        @Override
        public boolean retry(ITestResult result) {
            if (retryCount < maxRetry) {
                retryCount++;
                Loggers.logInfo(
                        "Retry " + retryCount + " for " + result.getName());
                return true;
            }
            return false;
        }
}