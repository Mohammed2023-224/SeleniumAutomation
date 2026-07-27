package engine.actions;

import engine.enums.WaitTypes;
import engine.reporters.Loggers;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static engine.actions.Waits.customWait;
import static engine.actions.Waits.fluentWaitShortTime;

public class ElementActions {
    private static final String ELEMENT_LOCATED_TEXT ="Element Located at";
    private static final String CLICK_TEXT ="Click ";

    private ElementActions(){}

    private static Actions actions(WebDriver driver) {
        return new Actions(driver);
    }

    public static void clickElement(WebDriver driver, By locator) {
        try {
            fluentWaitShortTime(driver).until(d -> {
                try {
                    d.findElement(locator).click();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            });
            Loggers.logInfo(CLICK_TEXT + ELEMENT_LOCATED_TEXT + ": " + locator);
        } catch (Exception e) {
            Loggers.logError("Couldn't click element located at: "+locator);
            throw e;
        }
    }

    public static void clickElementTillElementAppears(WebDriver driver, By locator,By nextElement) {
        try {
            fluentWaitShortTime(driver).until(d -> {
                try {
                    d.findElement(locator).click();
                    Thread.sleep(100);
                    return ElementActions.checkIfElementVisible(d, nextElement);
                } catch (Exception e) {
                    return false;
                }
            });
            Loggers.logInfo(CLICK_TEXT + ELEMENT_LOCATED_TEXT + ": " + locator);
        } catch (Exception e) {
            Loggers.logError("Couldn't click element located at: "+locator+" Or the next element didn't appear correctly which is located by: "
            +nextElement);
            throw e;
        }
    }

    public static void selectOption(WebDriver driver, By locator,String text) {
        try {
            Select select = new Select(driver.findElement(locator));
            select.selectByVisibleText(text);
            Loggers.logInfo("Select " + text + " from selection located at: " + locator);
        } catch (Exception e) {
            Loggers.logError("Couldn't select the option with visible text: "+ text);
            throw e;
        }
        }

    public static void selectOptionByValue(WebDriver driver, By locator,String text) {
        try {
            Select select = new Select(driver.findElement(locator));
            select.selectByValue(text);
            Loggers.logInfo("Select " + text + " from selection located at: " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't select the option with value: " + text);
        }
            }

    public static void selectDDLOptionText(WebDriver driver, By locator,String option){
        try {
            customWait(WaitTypes.ShortWait,"wait and Select option with text " + option + " from locator " + locator,
                    x -> {
                        Select dynamicSelect = new Select(driver.findElement(locator));
                        if (dynamicSelect.getOptions().isEmpty()) {
                            return false;
                        } else {
                            dynamicSelect.selectByVisibleText(option);
                            return true;
                        }
                    }    );
        }catch (Exception e) {
            Loggers.logError("Couldn't select the option with option: "+ option);
            throw e;
        }
    }

    public static void typeInElement(WebDriver driver, By locator, String text) {
        try{
        Waits.waitToBeClickable(locator);
        driver.findElement(locator).sendKeys(text);
        Loggers.logInfo("type " + text + " in element located at:" + locator);
        }catch (Exception e) {
            Loggers.logError("Couldn't type in element: "+ locator);
            throw e;
        }
    }

    public static void clearField(WebDriver driver, By locator) {
        try {
            Waits.waitToBeClickable( locator);
            driver.findElement(locator).clear();
            Loggers.logInfo("clear field located at " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't clear field: "+ locator);
            throw e;
        }
    }

    public static void clearFieldUsingKeyBoard(WebDriver driver, By locator) {
        try {
            Waits.waitToBeClickable( locator);
            clickElement(driver, locator);
            driver.findElement(locator).sendKeys(Keys.chord(Keys.CONTROL, "a"));
            driver.findElement(locator).sendKeys(Keys.DELETE);
            Waits.fluentWaitShortTime(driver).until(d -> {
                WebElement e = d.findElement(locator);
                String v = e.getAttribute("value");
                return v == null || v.isEmpty();
            });
            Loggers.logInfo("clear field located at " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't clear field located at: "+locator);
            throw e;
        }
    }

    public static String getText(WebDriver driver, By locator) {
        try {
            Waits.customWait(driver,"Wait for element visibility: "+locator,
                    ExpectedConditions.visibilityOfElementLocated(locator),2);
        } catch (Exception e) {
            Loggers.logWarn("Element isn't visible");
        }
        return Waits.fluentWaitShortTime(driver).until(d -> {
            try {
                String text=d.findElement(locator).getText();
                Loggers.logInfo("get text "+text +" out of element located at " + locator);
                return text;
            }catch (Exception e) {
                Loggers.logError("Couldn't get text in element: "+ locator);
                throw e;
            }
        });
    }

    public static String getTextWait(WebDriver driver, By locator) {
        try {
            return Waits.fluentWaitShortTime(driver).until(d -> {
                try {
                    return d.findElement(locator).getText();
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });
        } catch (Exception e) {
            Loggers.logError("Couldn't get text at element located at: "+locator);
            throw e;
        }
    }

    public static void scrollToElement(WebDriver driver, By locator) {
        try{
        Waits.waitToBeClickable(locator);
        actions(driver).scrollToElement(driver.findElement(locator)).perform();
        Loggers.logInfo("scroll to element located at: " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't scroll to element: "+ locator);
            throw e;
        }
    }

    public static void doubleClickElement(WebDriver driver, By locator) {
        try {
            Waits.waitToBeClickable(locator);
            actions(driver).doubleClick(driver.findElement(locator)).perform();
            Loggers.logInfo("double click element located at: " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't double click element located at: "+ locator);
            throw e;
        }
    }

    public static void hoverOverElement(WebDriver driver, By locator) {
        try {
            Waits.waitToBeVisible(driver, locator);
            actions(driver).moveToElement(driver.findElement(locator)).perform();
            Loggers.logInfo("hover over element located at: " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't hover over element located at: "+ locator);
            throw e;
        }
    }

    public static void pressKeyboardKeys(WebDriver driver, By locator, Keys key) {
        try {
            Waits.waitToBeClickable( locator);
            driver.findElement(locator).sendKeys(key);
            Loggers.logInfo("press keyboard key: " + key + " in element located at " + locator);
        }catch (Exception e) {
            Loggers.logError("Couldn't press keyboard key on element: "+ locator);
            throw e;
        }
    }

    public static void holdKeyboardKey(WebDriver driver, Keys key) {
        try {
            actions(driver).keyDown(key);
            Loggers.logInfo("hold keyboard key: " + key);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't hold the keyboard key: "+key);
            throw e;
        }
    }

    public static void unholdKeyboardKey(WebDriver driver, Keys key) {
        try {
            actions(driver).keyUp(key);
            Loggers.logInfo("unhold keyboard key: " + key);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't unhold the keyboard key: "+ key);
            throw e;
        }
    }

    public static void pressKeyboardKeys(WebDriver driver, Keys key) {
        try{
        actions(driver).sendKeys(key).perform();
        Loggers.logInfo("press keyboard key: "+key);}
        catch (Exception e) {
            Loggers.logError("Couldn't press the keyboard key: "+ key);
            throw e;
        }
    }

    public static String getElementAttribute(WebDriver driver, By locator, String property){
        try {
            Waits.waitToBeVisible(driver, locator);
            String attributeValue = driver.findElement(locator).getDomAttribute(property);
            Loggers.logInfo("Get the attribute value of " + property + " which equals : " + attributeValue);
            return attributeValue;
        } catch (Exception e) {
            Loggers.logError("Couldn't get the element attribute: "+ property+" for element located at: "+ locator);
            throw e;
        }
    }

    public static String getElementAttribute( WebElement locator, String property){
        try {
            String attributeValue = locator.getDomAttribute(property);
            Loggers.logInfo("Get the attribute value of " + property + " which equals : " + attributeValue);
            return attributeValue;
        }
        catch (Exception e) {
            Loggers.logError("Couldn't get the element attribute: "+ property+" for element located at: "+ locator);
            throw e;
        }
    }


    public static int countElement(WebDriver driver, By locator){
        try {
            Loggers.logInfo("Get number of element with the locator: " + locator + " =" + driver.findElements(locator).size());
            return driver.findElements(locator).size();
        }
        catch (Exception e) {
            Loggers.logError("Couldn't count element located at: "+ locator);
            throw e;
        }
    }

    public static void contextClickElement(WebDriver driver, By locator){
        try {
            Waits.waitToBeVisible(driver, locator);
            new Actions(driver).contextClick(driver.findElement(locator)).perform();
            Loggers.logInfo("right Clicking element: " + locator);
        }
        catch (Exception e) {
            Loggers.logError("Couldn't right click element located at: "+ locator);
            throw e;
        }
    }

    public static Boolean checkIfElementExists(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (!driver.findElements(locator).isEmpty()) {
                flag = true;
                Loggers.logInfo(ELEMENT_LOCATED_TEXT +":"+locator+" exists");
            }
        } catch (Exception e) {
            Loggers.logWarn(ELEMENT_LOCATED_TEXT +":"+locator+" doesn't exist");
        }
        return flag;
    }

    public static Boolean checkIfElementVisible(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (driver.findElement(locator).isDisplayed()) {
                flag = true;
                Loggers.logInfo(ELEMENT_LOCATED_TEXT +": "+locator+" is visible");
            }
        } catch (Exception e) {
            Loggers.logWarn(ELEMENT_LOCATED_TEXT +":"+locator+" isn't visible");
        }
        return flag;
    }

    public static Boolean checkIfElementInVisible(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (!driver.findElement(locator).isDisplayed()) {
                flag = true;
                Loggers.logInfo(ELEMENT_LOCATED_TEXT +": "+locator+" is invisible");
            }
        } catch (Exception e) {
            Loggers.logWarn(ELEMENT_LOCATED_TEXT +": "+locator+" is visible");
        }
        return flag;
    }

    public static Boolean checkIfElementClickable(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            WebElement ele=driver.findElement(locator);
            if (ele.isEnabled() && ele.isDisplayed()) {
                flag = true;
                Loggers.logInfo(ELEMENT_LOCATED_TEXT +": "+locator+" is clickable");
            }
        } catch (Exception e) {
            Loggers.logWarn(ELEMENT_LOCATED_TEXT +":"+locator+" isn't clickable");
        }
        return flag;
    }

    public static Boolean checkIfElementSelected(WebDriver driver, By locator) {
        boolean flag = false;
        try {
            if (driver.findElement(locator).isSelected()) {
                flag = true;
                Loggers.logInfo(ELEMENT_LOCATED_TEXT +":"+locator+" is selected");
            }
        } catch (Exception e) {
            Loggers.logWarn(ELEMENT_LOCATED_TEXT +":"+locator+" isn't selected");
        }
        return flag;
    }
}