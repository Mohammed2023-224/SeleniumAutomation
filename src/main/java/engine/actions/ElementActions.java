package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class ElementActions {

    private static Actions actions(WebDriver driver) {
        return new Actions(driver);
    }

    public static void clickElement(WebDriver driver, By locator) {
        String logs = "click element located at:  " + locator;
        driver.findElement(locator).click();
       Loggers.log.info(logs);
    }

    public static void selectOption(WebDriver driver, By locator,String text) {
        String logs = "Select "+text+" from selection located at:  " + locator;
        Select select=new Select(driver.findElement(locator));
        select.selectByValue(text);
       Loggers.log.info(logs);
    }

    public static void selectDDLOptionText(WebDriver driver, By locator,String option){
        Waits.explicitWaitShortTime(driver).until(x -> {
            Select dynamicSelect = new Select(driver.findElement(locator));
            return !dynamicSelect.getOptions().isEmpty();
        });
        Select select=new Select(driver.findElement(locator));
        select.selectByVisibleText(option);
     Loggers.log.info("Select option with text "+option+" from locator "+locator);
    }

    public static void typeInElement(WebDriver driver, By locator, String text) {
        String logs = "type " + text + " in element located at:" + locator;
        driver.findElement(locator).sendKeys(text);
       Loggers.log.info(logs);
    }
    public static void clearField(WebDriver driver, By locator) {
        String logs = "clear field located at " + locator ;
        driver.findElement(locator).clear();
       Loggers.log.info(logs);
    }

    public static String getText(WebDriver driver, By locator) {
        String text = driver.findElement(locator).getText();
        String logs = "get text "+text +" out of element located at " + locator;
       Loggers.log.info(logs);
        return text;
    }

    public static void scrollToElement(WebDriver driver, By locator) {
        actions(driver).scrollToElement(driver.findElement(locator)).perform();
       Loggers.log.info("scroll to element located at:" + locator);
    }

    public static void doubleClickElement(WebDriver driver, By locator) {
        actions(driver).doubleClick(driver.findElement(locator)).perform();
       Loggers.log.info("double click element located at:  "+ locator);
    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        actions(driver).moveToElement(driver.findElement(locator)).perform();
       Loggers.log.info("hover over element located at: "+ locator);
    }

    public static void pressKeyboardKeys(WebDriver driver, By locator, Keys key) {
        driver.findElement(locator).sendKeys(key);
       Loggers.log.info("press keyboard key: "+ key+" in element located at "+ locator);
    }
    public static void pressKeyboardKeys(WebDriver driver, Keys key) {
        actions(driver).sendKeys(key).perform();
       Loggers.log.info("press keyboard key: "+ key);
    }


    public static String getElementAttribute(WebDriver driver, By locator, String property){
        String attributeValue=driver.findElement(locator).getDomAttribute(property);
     Loggers.log.info("Get the attribute of "+property+" with value : " ,attributeValue);
        return attributeValue;
    }

    public static String getElementAttribute( WebElement locator, String property){
        String attributeValue=locator.getDomAttribute(property);
     Loggers.log.info("Get the attribute of web element "+property+" with value : ",attributeValue);
        return attributeValue;
    }

    public static void switchToFrameByLocator(WebDriver driver, By locator){
        driver.switchTo().frame(driver.findElement(locator));
     Loggers.log.info("switch to frame by locator: ",locator);
    }

    public static void contextClickElement(WebDriver driver, By locator){
        new Actions(driver).contextClick(driver.findElement(locator)).perform();
     Loggers.log.info(" right Clicking element: ",locator);
    }

    public static void switchToParentFrame(WebDriver driver){
        driver.switchTo().parentFrame();
     Loggers.log.info("switch to parent frame");
    }

    public static Boolean checkIfElementExists(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (!driver.findElements(locator).isEmpty()) {
                flag = true;
             Loggers.log.info("Element located at "+locator+" exists");
            }
        } catch (Exception e) {
         Loggers.log.warn("Element located at "+locator+" doesn't exist");
        }
        return flag;
    }

    public static Boolean checkIfElementVisible(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isDisplayed()) {
                flag = true;
             Loggers.log.info("Element located at: "+locator+" is visible");
            }
        } catch (Exception e) {
         Loggers.log.warn("Element located at "+locator+" isn't visible");
        }
        return flag;
    }


    public static Boolean checkIfElementClickable(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isEnabled() && driver.findElement(locator).isDisplayed()) {
                flag = true;
             Loggers.log.info("Element located at: "+locator+" is clickable");
            }
        } catch (Exception e) {
         Loggers.log.warn("Element located at "+locator+" isn't clickable");
        }
        return flag;
    }

    public static Boolean checkIfElementSelected(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (driver.findElement(locator).isSelected()) {
                flag = true;
             Loggers.log.info("Element located at {} is selected", locator);
            }
        } catch (Exception e) {
         Loggers.log.warn("Element located at {} isn't selected", locator);
        }
        return flag;
    }
}
