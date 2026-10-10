package engine.listeners;

import engine.assertions.SoftAssertContext;
import engine.assertions.SoftAssertManager;
import engine.reporters.Loggers;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.ThreadContext;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.util.List;

public class InvocationListeners implements IInvokedMethodListener {


    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {

        // 1. After the test method finishes, evaluate soft assertions.
        if (method.isTestMethod()) {
            try {
                SoftAssertManager.assertAllAndClear();
            } catch (AssertionError softFailure) {
                result.setStatus(ITestResult.FAILURE);

                Throwable existingThrowable = result.getThrowable();
                if (existingThrowable != null && existingThrowable != softFailure) {
                    existingThrowable.addSuppressed(softFailure);
                } else {
                    result.setThrowable(softFailure);
                }

                List<SoftAssertContext.SoftAssertScreenshot> screenshots =
                        SoftAssertContext.getScreenshots();

                for (SoftAssertContext.SoftAssertScreenshot screenshot : screenshots) {
                    Allure.getLifecycle().addAttachment(
                            screenshot.getName(),
                            "image/png",
                            "png",
                            screenshot.getBytes()
                    );
                }
            } finally {
                SoftAssertContext.clear();
            }

            return;
        }

        // 2. After the log-attachment @AfterMethod finishes, close the appender.
        if (method.isConfigurationMethod()
                && method.getTestMethod() != null
                && method.getTestMethod().isAfterMethodConfiguration()
                && method.getTestMethod().getMethodName().equals("attachLogsAndScreenshot")) {

            String fileName = ThreadContext.get("testLogFileName");

            if (fileName != null) {
                Loggers.cleanupPerTestAppender(fileName);
                ThreadContext.remove("testLogFileName");
            }
        }
    }

    }