package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class DragAndDropActions {
    private DragAndDropActions(){}
    public static void dragAndDropByMouse(WebDriver driver, By locatorSource, By locatorTarget){
        Waits.explicitWaitLongTime(driver).until(ExpectedConditions.elementToBeClickable(locatorSource));
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.elementToBeClickable(locatorTarget));
        WebElement source=driver.findElement(locatorSource);
        WebElement target=driver.findElement(locatorTarget);
        new Actions(driver).scrollToElement(source)
                .clickAndHold(source)
                .scrollToElement(target)
                .moveToElement(target)
                .release(target).build().perform();
        Loggers.getLogger().info(" drag element from {} to {} by mouse",locatorSource,locatorTarget);
    }

    public static void dragAndDropByLocation(WebDriver driver, By locatorSource ,int horizontal, int vertical){
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locatorSource));
        new Actions(driver).dragAndDropBy(driver.findElement(locatorSource),horizontal,vertical).perform();
        Loggers.getLogger().info(" drag element from {} to {} , {}",locatorSource,horizontal ,vertical);
    }
    public static void jsDragAndDrop(WebDriver driver, By locator,By position) {
        String logs = "drag "+locator+" to  " + position;
        String script = """
        function triggerDragAndDrop(selectorDrag, selectorDrop) {
          const drag = arguments[0], drop = arguments[1];
          const dataTransfer = new DataTransfer();
          drag.dispatchEvent(new DragEvent('dragstart', {dataTransfer}));
          drop.dispatchEvent(new DragEvent('drop', {dataTransfer}));
          drag.dispatchEvent(new DragEvent('dragend', {dataTransfer}));
        }
        triggerDragAndDrop(arguments[0], arguments[1]);
    """;
        ((JavascriptExecutor) driver).executeScript(script, driver.findElement(locator), driver.findElement(position));
        Loggers.getLogger().info(logs);
    }

    public static void jsDragAndDropAsHtml(WebDriver driver, By locator,By position) {
        String logs = "drag "+locator+" to  " + position;
        String script = """
        const src = arguments[0];
       const dest = arguments[1];
       const dataTransfer = new DataTransfer();
       const fireEvent = (type, elem) => {
           const event = new DragEvent(type, {
               bubbles: true,
               cancelable: true,
               dataTransfer: dataTransfer
           });
           elem.dispatchEvent(event);
       };
       fireEvent('dragstart', src);
       fireEvent('dragenter', dest);
       fireEvent('dragover', dest);
       fireEvent('drop', dest);
       fireEvent('dragend', src);
    """;
        ((JavascriptExecutor) driver).executeScript(script, driver.findElement(locator), driver.findElement(position));
        Loggers.getLogger().info(logs);
    }
    public static void dragAndDrop(WebDriver driver, By locator,By position) {
        String logs = "drag "+locator+" to  " + position;
        Waits.waitToBeClickable(driver, locator);
        new Actions(driver).dragAndDrop(driver.findElement(locator),driver.findElement(position) ).perform();
        Loggers.getLogger().info(logs);
    }

    public static void manualDragAndDrop(WebDriver driver, By locator,By position) {
        String logs = "drag "+locator+" to  " + position;
        Waits.waitToBeClickable(driver, locator);
        new  Actions(driver).moveToElement(driver.findElement(locator))
                .pause(Duration.ofMillis(200))
                .clickAndHold(driver.findElement(locator))
                .pause(Duration.ofMillis(300))
                .moveToElement(driver.findElement(position))
                .pause(Duration.ofMillis(300))
                .release(driver.findElement(position))
                .build()
                .perform();
        Loggers.getLogger().info(logs);
    }
}
