package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ElementActions {
    private ElementActions(){}

    private static Actions actions(WebDriver driver) {
        return new Actions(driver);
    }

    public static void clickElement(WebDriver driver, By locator) {
        String logs = "click element located at: " + locator;
        Waits.waitToBeClickable(driver,locator);
        driver.findElement(locator).click();
       Loggers.getLogger().info(logs);
    }

    public static void selectOption(WebDriver driver, By locator,String text) {
        String logs = "Select "+text+" from selection located at: " + locator;
        Select select=new Select(driver.findElement(locator));
        select.selectByVisibleText(text);
       Loggers.getLogger().info(logs);
    }

    public static void selectDDLOptionText(WebDriver driver, By locator,String option){
        Waits.explicitWaitShortTime(driver).until(x -> {
            Select dynamicSelect = new Select(driver.findElement(locator));
            return !dynamicSelect.getOptions().isEmpty();
        });
        Select select=new Select(driver.findElement(locator));
        select.selectByVisibleText(option);
     Loggers.getLogger().info("Select option with text {} from locator {}",option,locator);
    }

    public static void typeInElement(WebDriver driver, By locator, String text) {
        String logs = "type " + text + " in element located at:" + locator;
        Waits.waitToBeClickable(driver,locator);
        driver.findElement(locator).sendKeys(text);
       Loggers.getLogger().info(logs);
    }
    public static void clearField(WebDriver driver, By locator) {
        String logs = "clear field located at " + locator ;
        Waits.waitToBeClickable(driver,locator);
        driver.findElement(locator).clear();
       Loggers.getLogger().info(logs);
    }

    public static String getText(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver,locator);
        String text = driver.findElement(locator).getText();
        String logs = "get text "+text +" out of element located at " + locator;
       Loggers.getLogger().info(logs);
        return text;
    }

    public static void scrollToElement(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver,locator);
        actions(driver).scrollToElement(driver.findElement(locator)).perform();
       Loggers.getLogger().info("scroll to element located at: {}" , locator);
    }

    public static void doubleClickElement(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver,locator);
        actions(driver).doubleClick(driver.findElement(locator)).perform();
       Loggers.getLogger().info("double click element located at: {}", locator);
    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        Waits.waitToBeVisible(driver,locator);
        actions(driver).moveToElement(driver.findElement(locator)).perform();
       Loggers.getLogger().info("hover over element located at: {}", locator);
    }

    public static void pressKeyboardKeys(WebDriver driver, By locator, Keys key) {
        Waits.waitToBeClickable(driver,locator);
        driver.findElement(locator).sendKeys(key);
       Loggers.getLogger().info("press keyboard key: {} in element located at {}",key, locator);
    }
    public static void pressKeyboardKeys(WebDriver driver, Keys key) {
        actions(driver).sendKeys(key).perform();
       Loggers.getLogger().info("press keyboard key: {}", key);
    }


    public static String getElementAttribute(WebDriver driver, By locator, String property){
        Waits.waitToBeVisible(driver,locator);
        String attributeValue=driver.findElement(locator).getDomAttribute(property);
     Loggers.getLogger().info("Get the attribute value of {} which equals : {}",property ,attributeValue);
        return attributeValue;
    }

    public static String getElementAttribute( WebElement locator, String property){
        String attributeValue=locator.getDomAttribute(property);
     Loggers.getLogger().info("Get the attribute of web element {} with value : {}",property,attributeValue);
        return attributeValue;
    }

    public static void switchToFrameByLocator(WebDriver driver, By locator){
        driver.switchTo().frame(driver.findElement(locator));
     Loggers.getLogger().info("switch to frame by locator: {}",locator);
    }

    public static void contextClickElement(WebDriver driver, By locator){
        Waits.waitToBeVisible(driver,locator);
        new Actions(driver).contextClick(driver.findElement(locator)).perform();
     Loggers.getLogger().info("right Clicking element: {}",locator);
    }

    public static void switchToParentFrame(WebDriver driver){
        driver.switchTo().parentFrame();
     Loggers.getLogger().info("switch to parent frame");
    }

    public static Boolean checkIfElementExists(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (!driver.findElements(locator).isEmpty()) {
                flag = true;
             Loggers.getLogger().info("Element located at {} exists",locator);
            }
        } catch (Exception e) {
         Loggers.getLogger().warn("Element located at {} doesn't exist",locator);
        }
        return flag;
    }

    public static Boolean checkIfElementVisible(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (driver.findElement(locator).isDisplayed()) {
                flag = true;
             Loggers.getLogger().info("Element located at: {} is visible",locator);
            }
        } catch (Exception e) {
         Loggers.getLogger().warn("Element located at {} isn't visible",locator);
        }
        return flag;
    }


    public static Boolean checkIfElementClickable(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            WebElement ele=driver.findElement(locator);
            if (ele.isEnabled() && ele.isDisplayed()) {
                flag = true;
             Loggers.getLogger().info("Element located at: {}} is clickable",locator);
            }
        } catch (Exception e) {
         Loggers.getLogger().warn("Element located at {} isn't clickable",locator);
        }
        return flag;
    }

    public static Boolean checkIfElementSelected(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (driver.findElement(locator).isSelected()) {
                flag = true;
             Loggers.getLogger().info("Element located at {} is selected",locator );
            }
        } catch (Exception e) {
         Loggers.getLogger().warn("Element located at {} isn't selected",locator);
        }
        return flag;
    }
}
