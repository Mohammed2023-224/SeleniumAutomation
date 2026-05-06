package engine.assertions;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

public class HardAssertions {
    private HardAssertions() {}

    public static void assertVisible(WebDriver driver, By locator){
        AssertionsHelpers.assertTrueWithRetry(()->ElementActions.checkIfElementVisible(driver, locator)
                ,"Expected element to be VISIBLE, but it was not. Locator: " + locator);
    }

    public static void assertNotVisible(WebDriver driver,By locator){
        AssertionsHelpers.assertTrueWithRetry(()->!ElementActions.checkIfElementVisible(driver, locator)
                ,"Expected element to be inVISIBLE, but it was . Locator: " + locator
                );
    }

    public static void assertTextContains(WebDriver driver,By locator,String expectedSubstring){
        AssertionsHelpers.assertTrueWithRetry
                (()->ElementActions.getText(driver, locator).toLowerCase().contains(
                                expectedSubstring == null ? "" : expectedSubstring.toLowerCase())
                        ,"Expected element text to CONTAIN [" + expectedSubstring + "] but didn't contain  [" + ElementActions.getText(driver, locator) + "]. Locator: " + locator
                        );

    }

    public static void assertTrue(Supplier<Boolean> func, String logMessage){
        AssertionsHelpers.assertTrueWithRetry(func,"Assertion "+logMessage );
    }
    public static void assertTextEquals(String actual,String expected){
        AssertionsHelpers.assertTrueWithRetry
                (()->actual.equalsIgnoreCase(expected)
                        ,actual+expected+" to EQUAL (ignore case)");
    }

    public static void assertTextContains(String actual,String expectedSubstring){
        AssertionsHelpers.assertTrueWithRetry
                (()->actual.toLowerCase().contains(expectedSubstring.toLowerCase())
                        ,actual+expectedSubstring+ "to CONTAIN (ignore case)"
                       );

    }
}
