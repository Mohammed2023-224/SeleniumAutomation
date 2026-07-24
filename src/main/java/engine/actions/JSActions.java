package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JSActions {
    private JSActions(){}
    private  static JavascriptExecutor  jsExec(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    public static void clickUsingJavaScript(WebDriver driver, By locator) {
        try {
            jsExec(driver).executeScript("arguments[0].click();", driver.findElement(locator));
            Loggers.logInfo("click element located at: " + locator + " using java script ");
        } catch (Exception e) {
            Loggers.logError("Failed clicking element located at: "+locator);
            throw e;
        }
    }

    public static Object executeScript(WebDriver driver, String script) {
        try {
            Loggers.logInfo("execute java script: " + script);
            return jsExec(driver).executeScript(script);
        }catch (Exception e) {
            Loggers.logError("Failed executing script: "+script);
            throw e;
        }
    }

    public static Object executeScript(WebDriver driver, String script,WebElement element) {
        try {
            Loggers.logInfo("execute java script: " + script + " on element {}" + element);
            return jsExec(driver).executeScript(script, element);
        }catch (Exception e) {
            Loggers.logError("Failed executing script: "+script+ " on element located at: "+element);
            throw e;
        }
    }

    public static void scrollToElement(WebDriver driver,By element) {
        try {
            Loggers.logInfo("scroll using JS to the end of the element " + element);
            jsExec(driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", driver.findElement(element));
        }catch (Exception e) {
            Loggers.logError("Failed scrolling to element located at: "+element);
            throw e;
        }
    }

    public static WebElement getShadowElement(WebDriver driver, By shadowHost, String cssSelectorInsideShadowRoot) {
        try {
            Loggers.logInfo("get shadow element with cssSelector " + cssSelectorInsideShadowRoot + " and host " + shadowHost);
            return (WebElement) jsExec(driver).executeScript(
                    "return arguments[0].shadowRoot.querySelector(arguments[1])",
                    driver.findElement(shadowHost), cssSelectorInsideShadowRoot);
        }catch (Exception e) {
            Loggers.logError("Failed getting the shadow element with css: "+cssSelectorInsideShadowRoot);
            throw e;
        }
    }


    public static String getElementPropertyJSExecutor(WebDriver driver, By locator, String property){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String value = (String) js.executeScript("return arguments[0][arguments[1]];", driver.findElement(locator), property);
            Loggers.logInfo("Get the property of " + property + " with value : " + value);
            return value;
        }catch (Exception e) {
            Loggers.logError("Failed getting property: "+property+" for element located at: "+locator);
            throw e;
        }
    }

    public static String getCssValue(WebDriver driver, By locator,String property){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String value = (String) js.executeScript("return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", driver.findElement(locator), property);
            Loggers.logInfo("Get the css value " + property + " value : " + value);
            return value;
        }
        catch (Exception e) {
            Loggers.logError("Failed getting css value: "+property+" for element located at: "+locator);
            throw e;
        }
    }

    public static String getPseudoElementContent(WebDriver driver, By locator,String pseudoElement ){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String value = (String) js.executeScript("return window.getComputedStyle(arguments[0], arguments[1]).getPropertyValue('content');"
                    , driver.findElement(locator), pseudoElement);
            Loggers.logInfo("Get the pseudo element content : " + value + " from element " + locator);
            assert value != null;
            return value.replace("\"", "");
        }catch (Exception e) {
            Loggers.logError("Failed getting pseudo element content for element located at: "+locator);
            throw e;
        }

    }
}
