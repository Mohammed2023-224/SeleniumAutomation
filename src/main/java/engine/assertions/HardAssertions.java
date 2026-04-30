package engine.assertions;

import engine.actions.ElementActions;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class HardAssertions {
    private HardAssertions() {}

    public static void assertVisible(WebDriver driver, By locator){
        AssertionsHelpers.assertTrueWithRetry(ElementActions.checkIfElementVisible(driver, locator)
                ,"Expected element to be VISIBLE, but it was not. Locator: " + locator
                ,"Assertion: Element is visible: "+ locator);
    }

    public static void assertNotVisible(WebDriver driver,By locator){
        AssertionsHelpers.assertTrueWithRetry(!ElementActions.checkIfElementVisible(driver, locator)
                ,"Expected element to be inVISIBLE, but it was . Locator: " + locator
                ,"Assertion: Element is invisible: "+ locator);
    }

    public static void assertTextContains(WebDriver driver,By locator,String expectedSubstring){
        AssertionsHelpers.assertTrueWithRetry
                (ElementActions.getText(driver, locator).toLowerCase().contains(
                                expectedSubstring == null ? "" : expectedSubstring.toLowerCase())
                        ,"Expected element text to CONTAIN [" + expectedSubstring + "] but didn't contain  [" + ElementActions.getText(driver, locator) + "]. Locator: " + locator
                        ,"Assertion: Text contains. Expected part: ["+expectedSubstring+"], Actual: ["+ElementActions.getText(driver, locator)  +"], Locator: "+ locator);

    }

    private static String expectedLog(String actual,String expected,String message){
        return "Assertion: Expected [" + expected + "] "+message+" [" + actual + "]";
    }

    public static void assertTrue(boolean result,String logMessage){
        AssertionsHelpers.assertTrueWithRetry(result,"Assertion "+logMessage+" was wrong" ,
                "Assertion: "+ logMessage);
    }

    public static void assertTextEquals(String actual,String expected){
        AssertionsHelpers.assertTrueWithRetry
                (actual.equalsIgnoreCase(expected)
                        ,actual+expected+" to EQUAL (ignore case)"
                        ,"Assertion: Equals ignore case. Expected: ["+expected+"], Actual: ["+actual+"]");

    }

    public static void assertTextContains(String actual,String expectedSubstring){
        AssertionsHelpers.assertTrueWithRetry
                (actual.toLowerCase().contains(expectedSubstring.toLowerCase())
                        ,actual+expectedSubstring+ "to CONTAIN (ignore case)"
                        ,"Assertion: Contains ignore case. Expected part: ["+expectedSubstring+"], Actual: ["+actual+"]");

    }
}
