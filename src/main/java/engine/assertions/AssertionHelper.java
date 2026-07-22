package engine.assertions;

import engine.listeners.AllureAttachments;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.util.function.BooleanSupplier;

public class AssertionHelper {
    private AssertionHelper(){}

    public static void assertTrueWithRetry(BooleanSupplier fn,
                                           String assertionMessage) {
        int limit = 1;
        int count = 10;
        while (limit<=count) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Loggers.logError("Issue with thread couldn't sleep: "+ e);
            }
            boolean flag=  fn.getAsBoolean();
            if (flag) {
                Assert.assertTrue(true, assertionMessage);
                Loggers.logInfo(assertionMessage);
            } else {
                limit++;
            }
            if (count == limit) {
                Assert.fail("Tried asserting 10 times in 1 sec. no results. " + assertionMessage);
            }
        }
    }

    public static void softAssertTrueWithRetry(WebDriver driver ,BooleanSupplier fn,
                                               String assertionMessage) {
        SoftAssert softAssertions = SoftAssertManager.get();
        int limit = 1;
        int count = 5;
        while (limit<=count) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Loggers.logError("Issue with thread couldn't sleep: "+e);
            }
           boolean flag=  fn.getAsBoolean();
            if (flag) {
                softAssertions.assertTrue(true, assertionMessage);
                Loggers.logInfo(assertionMessage);
                return;
            } else {
                limit++;
            }
            if (count == limit) {
                softAssertions.assertFalse(
                        false,
                        "Soft Assertion: Tried asserting 10 times in 1 sec. no results. Assertion failed " + assertionMessage
                );
                AllureAttachments.saveScreensShot(driver,"failed assertion for: "+assertionMessage);
                AllureAttachments.saveScreensShotSoftAssertion(
                        driver,
                        "failed soft assertion - " + assertionMessage
                );
            }
        }
    }
}