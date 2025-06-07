package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ElementActions {

    public static void clickElement(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).click();
        Loggers.log.info("click element located at: {} ", locator);
    }

    public static void typeInElement(WebDriver driver, By locator, String text) {
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).sendKeys(text);
        Loggers.log.info("type {} in element located at: {} ", text, locator);
    }

    public static String getText(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver, locator);
        String text = driver.findElement(locator).getText();
        Loggers.log.info("get text {} out of element located at {}", text, locator);
        return text;
    }

    public static void clickUsingJavaScript(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver, locator);
        Helpers.initiateJSExecutor(driver).executeScript("arguments[0].click();", driver.findElement(locator));
        Loggers.log.info("click element located at: {} using java script ", locator);
    }


    // Check element actions
    public static Boolean checkIfElementExists(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElements(locator).size() > 0) {
                flag = true;
                Loggers.log.info("Element exists with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.log.info("element located at {} doesn't exist", locator);
        }
        return flag;
    }

    public static Boolean checkIfElementVisible(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isDisplayed()) {
                flag = true;
                Loggers.log.info("Element is visible with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.log.info("element located at {} isn't visible", locator);
        }
        return flag;
    }


    public static Boolean checkIfElementClickable(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isEnabled() && driver.findElement(locator).isDisplayed()) {
                flag = true;
                Loggers.log.info("Element is clickable with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.log.info("element clickable at {} is visible", locator);
        }
        return flag;
    }
}
