package engine.assertions;

import engine.listeners.AllureAttachments;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.util.function.BooleanSupplier;

public class AssertionHelper {
    private AssertionHelper(){}
    static final int  COUNT=10;

    public static void assertTrueWithRetry(BooleanSupplier fn,
                                           String assertionMessage) {

        for (int attempt = 1; attempt <= COUNT; attempt++) {
            boolean flag = fn.getAsBoolean();
            if (flag) {
                Loggers.logInfo(assertionMessage);
                return;
            }
            if (attempt == COUNT) {
                Assert.fail(
                        "Tried asserting " + COUNT +
                                " times. Assertion failed: " + assertionMessage
                );
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Loggers.logError("Thread interrupted while retrying assertion"+ e.getMessage());
                return;
            }
        }
    }

    public static void softAssertTrueWithRetry(WebDriver driver ,BooleanSupplier fn,
                                               String assertionMessage) {
        SoftAssert softAssertions = SoftAssertManager.get();
        for (int attempt = 1; attempt <= COUNT; attempt++) {

            boolean flag = fn.getAsBoolean();
            if (flag) {
                softAssertions.assertTrue(true, assertionMessage);
                return;
            }
            if (attempt == COUNT) {
                softAssertions.assertFalse(
                        false,
                        "Soft Assertion: Tried asserting " + COUNT +
                                " times. Assertion failed: " + assertionMessage
                );
                AllureAttachments.saveScreensShot(
                        driver,
                        "failed assertion for: " + assertionMessage
                );
                AllureAttachments.saveScreensShotSoftAssertion(
                        driver,
                        "failed soft assertion - " + assertionMessage
                );
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Loggers.logError("Thread interrupted while retrying assertion "+ e.getMessage());
                return;
            }
        }
    }
}