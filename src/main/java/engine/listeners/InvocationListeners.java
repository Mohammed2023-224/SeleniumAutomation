package engine.listeners;

import engine.assertions.SoftAssertContext;
import engine.assertions.SoftAssertManager;
import io.qameta.allure.Allure;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.util.List;

public class InvocationListeners implements IInvokedMethodListener {


    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (!method.isTestMethod()) {
            return;
        }

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
    }

}