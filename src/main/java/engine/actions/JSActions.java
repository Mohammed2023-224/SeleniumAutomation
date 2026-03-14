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
        jsExec(driver).executeScript("arguments[0].click();", driver.findElement(locator));
        Loggers.getLogger().info("click element located at: {} using java script ",locator);
    }

    public static Object executeScript(WebDriver driver, String script) {
        Loggers.getLogger().info("execute java script: {}",script);
        return jsExec(driver).executeScript(script);
    }
    public static Object executeScript(WebDriver driver, String script,WebElement element) {
        Loggers.getLogger().info("execute java script: {} on element {}",script,element);
        return jsExec(driver).executeScript(script,element);
    }

    public static void scrollToElement(WebDriver driver,By element) {
        Loggers.getLogger().info("scroll using JS to the end of the element {}",element);
         jsExec(driver).executeScript("arguments[0].scrollTop = arguments[0].scrollHeight",driver.findElement(element));
    }

    public static WebElement getShadowElement(WebDriver driver, By shadowHost, String cssSelectorInsideShadowRoot) {
        Loggers.getLogger().info("get shadow element with cssSelector {} and host {}",cssSelectorInsideShadowRoot, shadowHost);
        return (WebElement) jsExec(driver).executeScript(
                "return arguments[0].shadowRoot.querySelector(arguments[1])",
                driver.findElement(shadowHost), cssSelectorInsideShadowRoot);
    }


    public static String getElementPropertyJSExecutor(WebDriver driver, By locator, String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return arguments[0][arguments[1]];", driver.findElement(locator), property);
        Loggers.getLogger().info("Get the property of {}} with value : {}",property,value);
        return value;
    }
    public static String getCssValue(WebDriver driver, By locator,String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", driver.findElement(locator), property);
        Loggers.getLogger().info("Get the css value {} value : {}",property,value);
        return value;
    }
    public static String getPseudoElementContent(WebDriver driver, By locator,String pseudoElement ){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return window.getComputedStyle(arguments[0], arguments[1]).getPropertyValue('content');"
                , driver.findElement(locator),pseudoElement);
        Loggers.getLogger().info("Get the pseudo element content : {}} from element {}",value,locator);
        assert  value!=null;
        return value.replace("\"","");

    }
}
