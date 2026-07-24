package engine.assertions;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class HardAssertions {
    private HardAssertions() {}

    public static void assertVisible(WebDriver driver, By locator){
        AssertionHelper.assertTrueWithRetry(()->ElementActions.checkIfElementVisible(driver, locator)
                ,"Expected element "+locator+" to be VISIBLE" );
    }

    public static void assertNotVisible(WebDriver driver,By locator){
        AssertionHelper.assertTrueWithRetry(()->ElementActions.checkIfElementInVisible(driver, locator)
                ,"Expected element "+ locator+"to be inVISIBLE" );
    }

    public static void assertTextContains(WebDriver driver,By locator,String expectedSubstring){
        AssertionHelper.assertTrueWithRetry
                (()->ElementActions.getText(driver, locator).toLowerCase().contains(
                                expectedSubstring == null ? "" : expectedSubstring.toLowerCase())
                        ,"Expected element text in element "+locator+" to CONTAIN [" + expectedSubstring + "], and the element text is  [" + ElementActions.getText(driver, locator) + "]"
                );

    }

    public static void assertTru(BooleanSupplier func, String logMessage){
        AssertionHelper.assertTrueWithRetry(func,"Assertion "+logMessage );
    }

    public static void assertTextEquals(String actual,String expected){
        AssertionHelper.assertTrueWithRetry
                (()->actual.equalsIgnoreCase(expected)
                        ,"Ignoring case Expected "+actual+" to EQUAL "+expected);

    }

    public static void assertTextContains(String actual,String expectedSubstring){
        AssertionHelper.assertTrueWithRetry
                (()->actual.toLowerCase().contains(expectedSubstring.toLowerCase())
                        , "Ignoring case expected "+actual+" to CONTAIN "+expectedSubstring);

    }

}