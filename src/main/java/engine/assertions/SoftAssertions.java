package engine.assertions;

import engine.actions.ElementActions;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class SoftAssertions {

    private final SoftAssert sa = new SoftAssert();

    public void assertVisible(WebDriver driver, By locator) {
        boolean visible = ElementActions.checkIfElementVisible(driver, locator);
        sa.assertTrue(visible, "Expected element to be VISIBLE, but it was NOT. Locator: " + locator);
        Loggers.log.info("🟡(soft) Visible? {} → {}", locator, visible);
    }

    public void assertNotVisible(WebDriver driver, By locator) {
        boolean visible = ElementActions.checkIfElementVisible(driver, locator);
        sa.assertFalse(visible, "Expected element to be NOT VISIBLE, but it WAS. Locator: " + locator);
        Loggers.log.info("🟡(soft) Not visible? {} → {}", locator, !visible);
    }

    public void assertTextContains(WebDriver driver, By locator, String expectedSubstring) {
        String actual = ElementActions.getText(driver, locator);
        boolean ok = actual.toLowerCase()
                        .contains(expectedSubstring == null ? "" : expectedSubstring.toLowerCase());
        sa.assertTrue(ok,
                "Expected element text to CONTAIN [" + expectedSubstring + "] but was [" + actual + "]. Locator: " + locator);
        Loggers.log.info("🟡(soft) Text contains? Expected [{}], Actual [{}], Locator {}", expectedSubstring, actual, locator);
    }

    public void assertTextEquals(WebDriver driver, By locator, String expected) {
        String actual = ElementActions.getText(driver, locator);
        boolean ok = actual.trim().equalsIgnoreCase(expected == null ? "" : expected.trim());
        sa.assertTrue(ok,
                "Expected element text to EQUAL [" + expected + "] but was [" + actual + "]. Locator: " + locator);
        Loggers.log.info("🟡(soft) Text equals? Expected [{}], Actual [{}], Locator {}", expected, actual, locator);
    }

    public void assertLessThan(int actual, int threshold) {
        sa.assertTrue(actual < threshold, "Expected [" + actual + "] < [" + threshold + "]");
    }

    public void assertGreaterThan(int actual, int threshold) {
        sa.assertTrue(actual > threshold, "Expected [" + actual + "] > [" + threshold + "]");
    }

    /** Must be called once per test to report all soft failures. */
    public void assertAll() {
        sa.assertAll();
    }
}
