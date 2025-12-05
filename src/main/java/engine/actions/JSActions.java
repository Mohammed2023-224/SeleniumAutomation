package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JSActions {
    private  static JavascriptExecutor  jsExec(WebDriver driver) {
        return (JavascriptExecutor) driver;
    }

    public static void clickUsingJavaScript(WebDriver driver, By locator) {
        jsExec(driver).executeScript("arguments[0].click();", driver.findElement(locator));
        Loggers.log.info("click element located at: "+locator+" using java script ");
    }

    public static Object executeScript(WebDriver driver, String script) {
        Loggers.log.info("execute java script: "+script);
        return jsExec(driver).executeScript(script);
    }
    public static Object executeScript(WebDriver driver, String script,WebElement element) {
        Loggers.log.info("execute java script: "+script);
        return jsExec(driver).executeScript(script,element);
    }

    public static WebElement getShadowElement(WebDriver driver, By shadowHost, String cssSelectorInsideShadowRoot) {
        Loggers.log.info("get shadow element with cssSelector "+cssSelectorInsideShadowRoot+" and host ", shadowHost);
        return (WebElement) jsExec(driver).executeScript(
                "return arguments[0].shadowRoot.querySelector(arguments[1])",
                driver.findElement(shadowHost), cssSelectorInsideShadowRoot);
    }


    public static String getElementPropertyJSExecutor(WebDriver driver, By locator, String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return arguments[0][arguments[1]];", driver.findElement(locator), property);
        Loggers.log.info("Get the property of "+property+" with value : ",value);
        return value;
    }
    public static String getCssValue(WebDriver driver, By locator,String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", driver.findElement(locator), property);
        Loggers.log.info("Get the css value "+property+" value : ",value);
        return value;
    }
    public static String getPseudoElementContent(WebDriver driver, By locator,String pseudoElement ){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return window.getComputedStyle(arguments[0], arguments[1]).getPropertyValue('content');"
                , driver.findElement(locator),pseudoElement);
        Loggers.log.info("Get the pseudo element content : "+value+" from element  ",locator);
        return value.replace("\"","");
    }
}
