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
       Loggers.addInfoAndAllureStep(logs);
    }

    public static void selectOption(WebDriver driver, By locator,String text) {
        String logs = "Select "+text+" from selection located at:  " + locator;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        Select select=new Select(driver.findElement(locator));
        select.selectByValue(text);
       Loggers.addInfoAndAllureStep(logs);
    }
    public static void selectDDLOptionText(WebDriver driver, By locator,String option){
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        Waits.explicitWaitShortTime(driver).until(x -> {
            Select dynamicSelect = new Select(driver.findElement(locator));
            return !dynamicSelect.getOptions().isEmpty();
        });
        Select select=new Select(driver.findElement(locator));
        select.selectByVisibleText(option);
     Loggers.log.info("Select option with text "+option+" from locator "+locator);
    }

    public static void dragAndDrop(WebDriver driver, By locator,By position) {
        String logs = "drag "+locator+" to  " + position;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        new Actions(driver).dragAndDrop(driver.findElement(locator),driver.findElement(position) ).perform();
       Loggers.addInfoAndAllureStep(logs);
    }

    public static void typeInElement(WebDriver driver, By locator, String text) {
        String logs = "type " + text + " in element located at:" + locator;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).sendKeys(text);
       Loggers.addInfoAndAllureStep(logs);
    }
    public static void clearField(WebDriver driver, By locator) {
        String logs = "clear field located at " + locator ;
        scrollToElement(driver,locator);
        Waits.waitToBeClickable(driver, locator);
        driver.findElement(locator).clear();
       Loggers.addInfoAndAllureStep(logs);
    }

    public static String getText(WebDriver driver, By locator) {
        Waits.waitToBeClickable(driver, locator);
        String text = driver.findElement(locator).getText();
        String logs = "get text "+text +" out of element located at " + locator;
       Loggers.addInfoAndAllureStep(logs);
        return text;
    }

    public static void clickUsingJavaScript(WebDriver driver, By locator) {
        Helpers.initiateJSExecutor(driver).executeScript("arguments[0].click();", driver.findElement(locator));
       Loggers.addInfoAndAllureStep("click element located at: "+locator+" using java script ");
    }

    public static void scrollToElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
         Helpers.seleniumActions(driver).scrollToElement(driver.findElement(locator)).perform();
       Loggers.addInfoAndAllureStep("scroll to element located at:" + locator);

    }

    public static void doubleClickElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).doubleClick(driver.findElement(locator)).perform();
       Loggers.addInfoAndAllureStep("double click element located at:  "+ locator);

    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        Waits.waitToExist(driver, locator);
        Helpers.seleniumActions(driver).moveToElement(driver.findElement(locator)).perform();
       Loggers.addInfoAndAllureStep("hover over element located at: "+ locator);

    }

    public static void pressKeyboardKeys(WebDriver driver, By locator, Keys key) {
        Waits.waitToExist(driver, locator);
        driver.findElement(locator).sendKeys(key);
       Loggers.addInfoAndAllureStep("press keyboard key: "+ key+" in element located at "+ locator);
    }
    public static void pressKeyboardKeys(WebDriver driver, Keys key) {
        Helpers.seleniumActions(driver).sendKeys(key).perform();
       Loggers.addInfoAndAllureStep("press keyboard key: "+ key);
    }

    public static WebElement getShadowElement(WebDriver driver, By shadowHost, String cssSelectorInsideShadowRoot) {
     Loggers.log.info("get shadow element with cssSelector "+cssSelectorInsideShadowRoot+" and host ", shadowHost);
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
     Loggers.log.info(" drag element from "+locatorSource+" to "+locatorTarget+" by mouse");
    }

    public static void dragAndDropByLocation(WebDriver driver, By locatorSource ,int horizontal, int vertical){
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locatorSource));
        new Actions(driver).dragAndDropBy(driver.findElement(locatorSource),horizontal,vertical).perform();
     Loggers.log.info(" drag element from "+locatorSource+" to "+horizontal+" , " +vertical);
    }
    public static String getElementPropertyJSExecutor(WebDriver driver, By locator, String property){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String value= (String) js.executeScript("return arguments[0][arguments[1]];", driver.findElement(locator), property);
     Loggers.log.info("Get the property of "+property+" with value : ",value);
        return value;
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
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.elementToBeClickable(locator));
        new Actions(driver).contextClick(driver.findElement(locator)).perform();
     Loggers.log.info(" right Clicking element: ",locator);
    }
    public static void switchToParentFrame(WebDriver driver){
        driver.switchTo().parentFrame();
     Loggers.log.info("switch to parent frame");
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
    // Check element actions
    public static Boolean checkIfElementExists(WebDriver driver, By locator) {
        Boolean flag = false;
        try {
            if (driver.findElements(locator).size() > 0) {
                flag = true;
             Loggers.log.info("Element located at "+locator+" exists");
            }
        } catch (Exception e) {
         Loggers.log.info("Element located at "+locator+" doesn't exist");
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
         Loggers.log.info("Element located at "+locator+" isn't visible");
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
         Loggers.log.info("Element located at "+locator+" isn't clickable");
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
         Loggers.log.info("Element located at {} isn't selected", locator);
        }
        return flag;
    }
}
