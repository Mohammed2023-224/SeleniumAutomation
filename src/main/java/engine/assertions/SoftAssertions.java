package engine.assertions;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class SoftAssertions {
    private SoftAssertions() {}
    public static void assertVisible(WebDriver driver, By locator){
        AssertionHelper.softAssertTrueWithRetry(driver,()->ElementActions.checkIfElementVisible(driver, locator)
                ,"Expected element "+locator+" to be VISIBLE" );
    }

    public static void assertNotVisible(WebDriver driver,By locator){
        AssertionHelper.softAssertTrueWithRetry(driver,()->ElementActions.checkIfElementInVisible(driver, locator)
                ,"Expected element "+ locator+"to be inVISIBLE" );
    }

    public static void assertTextContains(WebDriver driver,By locator,String expectedSubstring){
        AssertionHelper.softAssertTrueWithRetry(driver,
                (()->ElementActions.getText(driver, locator).toLowerCase().contains(
                        expectedSubstring == null ? "" : expectedSubstring.toLowerCase()))
                ,"Expected element text in element "+locator+" to CONTAIN [" + expectedSubstring + "], and the element text is  [" + ElementActions.getText(driver, locator) + "]"
        );    }

    public static void assertTru(WebDriver driver, BooleanSupplier func, String logMessage){
        AssertionHelper.softAssertTrueWithRetry(driver,func,"Assertion "+logMessage);
    }

    public static void assertTextEquals(WebDriver driver,String actual,String expected){
        AssertionHelper.softAssertTrueWithRetry
                (driver,()->actual.equalsIgnoreCase(expected)
                        ,"Ignoring case Expected "+actual+" to EQUAL "+expected);
    }

    public static void assertTextContains(WebDriver driver,String actual,String expectedSubstring){
        AssertionHelper.softAssertTrueWithRetry
                (driver,()->actual.toLowerCase().contains(expectedSubstring.toLowerCase())
                        , "Ignoring case expected "+actual+" to CONTAIN "+expectedSubstring);
    }

}