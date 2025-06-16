package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

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

    public static void scrollToElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).scrollToElement(driver.findElement(locator)).perform();
        Loggers.log.info("scroll to element located at: {}", locator);
    }

    public static void doubleClickElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).doubleClick(driver.findElement(locator)).perform();
        Loggers.log.info("double click element located at: {} ", locator);
    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).moveToElement(driver.findElement(locator)).perform();
        Loggers.log.info("hover over element located at: {} ", locator);
    }

    public static void pressKeyboardKeys(WebDriver driver, By locator, Keys key) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).sendKeys(key);
        Loggers.log.info("press keyboard key: {} in element located at {}",key, locator);
    }

    public static WebElement getShadowElement(WebDriver driver, By shadowHost, String cssSelectorInsideShadowRoot) {
        Loggers.log.info("get shadow element with cssSelector {} and host {}",cssSelectorInsideShadowRoot,shadowHost );
        return (WebElement) Helpers.initiateJSExecutor(driver).executeScript(
                "return arguments[0].shadowRoot.querySelector(arguments[1])",
        driver.findElement(shadowHost), cssSelectorInsideShadowRoot);
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

    public static Boolean checkIfElementSelected(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isSelected() ) {
                flag = true;
                Loggers.log.info("Element is selected with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.log.info("element isn't selected at {} is visible", locator);
        }
        return flag;
    }
}
