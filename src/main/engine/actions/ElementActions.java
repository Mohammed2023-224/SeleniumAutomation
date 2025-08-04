package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class ElementActions {

    public static void clickElement(WebDriver driver, By locator) {
        String logs = "click element located at:  " + locator;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).click();
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }

    public static void selectOption(WebDriver driver, By locator,String text) {
        String logs = "Select "+text+" from selection located at:  " + locator;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        Select select=new Select(driver.findElement(locator));
        select.selectByValue(text);
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }
    public static void selectDDLOptionText(WebDriver driver, By locator,String option){
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        Waits.explicitWaitShortTime(driver).until(x -> {
            Select dynamicSelect = new Select(driver.findElement(locator));
            return !dynamicSelect.getOptions().isEmpty();
        });
        Select select=new Select(driver.findElement(locator));
        select.selectByVisibleText(option);
        Loggers.getInstance().log.info("Select option with text [{}] from locator [{}]",option,locator);
    }

    public static void dragAndDrop(WebDriver driver, By locator,By position) {
        String logs = "drag "+locator+" to  " + position;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        new Actions(driver).dragAndDrop(driver.findElement(locator),driver.findElement(position) ).perform();
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }

    public static void typeInElement(WebDriver driver, By locator, String text) {
        String logs = "type " + text + " in element located at:" + locator + "";
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).sendKeys(text);
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }
    public static void clearField(WebDriver driver, By locator) {
        String logs = "cleat field located at " + locator ;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).clear();
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }

    public static String getText(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver, locator);
        String text = driver.findElement(locator).getText();
        String logs = "get text "+text +" out of element located at " + locator;
        Loggers.getInstance().addInfoAndAllureStep(logs);
        return text;
    }

    public static void clickUsingJavaScript(WebDriver driver, By locator) {
        Helpers.initiateJSExecutor(driver).executeScript("arguments[0].click();", driver.findElement(locator));
        Loggers.getInstance().addInfoAndAllureStep("click element located at: "+locator+" using java script ");
    }

    public static void scrollToElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
         Helpers.seleniumActions(driver).scrollToElement(driver.findElement(locator)).perform();
        Loggers.getInstance().addInfoAndAllureStep("scroll to element located at:" + locator);

    }

    public static void doubleClickElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).doubleClick(driver.findElement(locator)).perform();
        Loggers.getInstance().addInfoAndAllureStep("double click element located at:  "+ locator);

    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).moveToElement(driver.findElement(locator)).perform();
        Loggers.getInstance().addInfoAndAllureStep("hover over element located at: "+ locator);

    }

    public static void pressKeyboardKeys(WebDriver driver, By locator, Keys key) {
        Waits.waitToExist(driver, locator);
        driver.findElement(locator).sendKeys(key);
        Loggers.getInstance().addInfoAndAllureStep("press keyboard key: "+ key+" in element located at "+ locator);
    }
    public static void pressKeyboardKeys(WebDriver driver, Keys key) {
        Helpers.seleniumActions(driver).sendKeys(key).perform();
        Loggers.getInstance().addInfoAndAllureStep("press keyboard key: "+ key);
    }

    public static WebElement getShadowElement(WebDriver driver, By shadowHost, String cssSelectorInsideShadowRoot) {
        Loggers.getInstance().log.info("get shadow element with cssSelector {} and host {}", cssSelectorInsideShadowRoot, shadowHost);
        return (WebElement) Helpers.initiateJSExecutor(driver).executeScript(
                "return arguments[0].shadowRoot.querySelector(arguments[1])",
                driver.findElement(shadowHost), cssSelectorInsideShadowRoot);
    }

    public static void dragAndDropByMouse(WebDriver driver, By locatorSource, By locatorTarget){
        Waits.explicitWaitLongTime(driver).until(ExpectedConditions.elementToBeClickable(locatorSource));
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.elementToBeClickable(locatorTarget));
        new Actions(driver).scrollToElement(driver.findElement(locatorSource))
                .clickAndHold(driver.findElement(locatorSource))
                .scrollToElement(driver.findElement(locatorTarget))
                .moveToElement(driver.findElement(locatorTarget))
                .release(driver.findElement(locatorTarget)).build().perform();
        Loggers.getInstance().log.info(" drag element from [{}] to [{}] by mouse",locatorSource,locatorTarget);
    }

    public static void dragAndDropByLocation(WebDriver driver, By locatorSource ,int horizontal, int vertical){
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locatorSource));
        new Actions(driver).dragAndDropBy(driver.findElement(locatorSource),horizontal,vertical).perform();
        Loggers.getInstance().log.info(" drag element from [{}]",locatorSource);
    }
    public static String getElementPropertyJSExecutor(WebDriver driver, By locator, String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return arguments[0][arguments[1]];", driver.findElement(locator), property);
        Loggers.getInstance().log.info("Get the property [{}] value : [{}]", property,value);
        return value;
    }

    public static String getElementAttribute(WebDriver driver, By locator, String property){
        String attributeValue=driver.findElement(locator).getDomAttribute(property);
        Loggers.getInstance().log.info("Get the attribute of [{}] value : [{}]", property,attributeValue);
        return attributeValue;
    }
    public static String getElementAttribute( WebElement locator, String property){
        String attributeValue=locator.getDomAttribute(property);
        Loggers.getInstance().log.info("Get the attribute of web element [{}] value : [{}]", property,attributeValue);
        return attributeValue;
    }
    public static void switchToFrameByLocator(WebDriver driver, By locator){
        driver.switchTo().frame(driver.findElement(locator));
        Loggers.getInstance().log.info("switch to frame by locator: [{}]",locator);
    }
    public static void contextClickElement(WebDriver driver, By locator){
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.elementToBeClickable(locator));
        new Actions(driver).contextClick(driver.findElement(locator)).perform();
        Loggers.getInstance().log.info(" right Clicking element: [{}]",locator);
    }
    public static void switchToParentFrame(WebDriver driver){
        driver.switchTo().parentFrame();
        Loggers.getInstance().log.info("switch to parent frame");
    }
    public static String getCssValue(WebDriver driver, By locator,String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return window.getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", driver.findElement(locator), property);
        Loggers.getInstance().log.info("Get the css value [{}] value : [{}]", property,value);
        return value;
    }
    public static String getPseudoElementContent(WebDriver driver, By locator,String pseudoElement ){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return window.getComputedStyle(arguments[0], arguments[1]).getPropertyValue('content');"
                , driver.findElement(locator),pseudoElement);
        Loggers.getInstance().log.info("Get the pseudo element content : [{}] from element [{}] ", value,locator);
        return value.replace("\"","");
    }
    // Check element actions
    public static Boolean checkIfElementExists(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElements(locator).size() > 0) {
                flag = true;
                Loggers.getInstance().log.info("Element exists with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.getInstance().log.info("element located at {} doesn't exist", locator);
        }
        return flag;
    }

    public static Boolean checkIfElementVisible(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isDisplayed()) {
                flag = true;
                Loggers.getInstance().log.info("Element is visible with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.getInstance().log.info("element located at {} isn't visible", locator);
        }
        return flag;
    }


    public static Boolean checkIfElementClickable(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isEnabled() && driver.findElement(locator).isDisplayed()) {
                flag = true;
                Loggers.getInstance().log.info("Element is clickable with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.getInstance().log.info("element clickable at {} is visible", locator);
        }
        return flag;
    }

    public static Boolean checkIfElementSelected(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElement(locator).isSelected()) {
                flag = true;
                Loggers.getInstance().log.info("Element is selected with locator: {}", locator);
            }
        } catch (Exception e) {
            Loggers.getInstance().log.info("element isn't selected at {} is visible", locator);
        }
        return flag;
    }
}
