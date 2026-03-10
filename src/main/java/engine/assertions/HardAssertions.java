package engine.assertions;

import engine.actions.ElementActions;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class HardAssertions {
    private HardAssertions() {}
    public static void assertVisible(WebDriver driver, By locator){
        boolean visible = ElementActions.checkIfElementVisible(driver, locator);
        Assert.assertTrue(
                visible,
                "Expected element to be VISIBLE, but it was not. Locator: " + locator
        );
        Loggers.getLogger().info(" Element is visible: {}", locator);
    }

    public static void assertNotVisible(WebDriver driver,By locator){

        boolean visible = ElementActions.checkIfElementVisible(driver, locator);
        Assert.assertFalse(
                visible,
                "Expected element to be NOT VISIBLE, but it was. Locator: " + locator
        );
        Loggers.getLogger().info("Element is not visible: {}", locator);

    }

    public static void assertTextContains(WebDriver driver,By locator,String expectedSubstring){
        String actual = ElementActions.getText(driver, locator);
        boolean ok = actual.toLowerCase().contains(
                        expectedSubstring == null ? "" : expectedSubstring.toLowerCase());
        Assert.assertTrue(
                ok,
                "Expected element text to CONTAIN [" + expectedSubstring + "] but didn't contain  [" + actual + "]. Locator: " + locator);
        Loggers.getLogger().info("Text contains. Expected part: [{}], Actual: [{}], Locator: {}", expectedSubstring, actual, locator);
    }

    public static void assertTextEquals(WebDriver driver,By locator,String expected){
        String actual = ElementActions.getText(driver, locator);
        boolean ok = actual.trim().equalsIgnoreCase(expected == null ? "" : expected.trim());
        Assert.assertTrue(
                ok,
                "Expected element text to EQUAL [" + expected + "] but wasn't equal to [" + actual + "]. Locator: " + locator);
        Loggers.getLogger().info("Text equals. Expected: [{}], Actual: [{}], Locator: {}", expected, actual, locator);

    }

    private static String expectedLog(String actual,String expected,String message){
        return "Expected [" + expected + "] "+message+" [" + actual + "]";
    }
    public static void assertNumberIsLowerThanGeneratedNumber(int actual,int threshold){
        Assert.assertTrue(
                actual < threshold,
                expectedLog(String.valueOf(actual),String.valueOf(threshold),"to be < ")
        );
        Loggers.getLogger().info("[{}] < [{}]", actual, threshold);

    }
    public static void assertTextEquals(String actual,String expected){
        Assert.assertTrue(
                actual.equalsIgnoreCase(expected),
                expectedLog(actual,expected,"to EQUAL (ignore case)" )
        );
        Loggers.getLogger().info("Equals ignore case. Expected: [{}], Actual: [{}]", expected, actual);

    }
    public static void assertTextContains(String actual,String expectedSubstring){
        Assert.assertTrue(
                actual.toLowerCase().contains(expectedSubstring.toLowerCase()),
                expectedLog(actual,expectedSubstring,"to CONTAIN (ignore case)")
        );
        Loggers.getLogger().info("Contains ignore case. Expected part: [{}], Actual: [{}]", expectedSubstring, actual);
    }


    public static void assertNumberIsHigherThanGeneratedNumber(int actual,int threshold){
        Assert.assertTrue(
                actual > threshold,
                expectedLog(String.valueOf(actual),String.valueOf(threshold),"to be > ")
        );
        Loggers.getLogger().info(" [{}] > [{}]", actual, threshold);

    }
}
