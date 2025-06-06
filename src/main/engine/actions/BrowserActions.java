package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class BrowserActions {

    public static void navigateTo(WebDriver driver, String url) {
        driver.navigate().to(url);
        Loggers.log.info("Navigated to url: {}", url);
    }

    public static void navigateBack(WebDriver driver) {
        driver.navigate().back();
        Loggers.log.info("Navigated to last page");
    }

    public static void navigateForward(WebDriver driver) {
        driver.navigate().forward();
        Loggers.log.info("Navigated forward");
    }

    public static void switchIframe(WebDriver driver, By frameLocator) {
        driver.switchTo().frame(driver.findElement(frameLocator));
        Loggers.log.info("Switch to iframe located at: {}", frameLocator);
    }

    public static void switchParentFrame(WebDriver driver) {
        driver.switchTo().parentFrame();
        Loggers.log.info("Switch to main frame");
    }

    public static void switchToWindowByIndex(WebDriver driver, int windowNumber) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windowNumber >= 0 && windowNumber < windows.size()) {
            driver.switchTo().window(windows.get(windowNumber));
        }
        Loggers.log.info("Switch to windows number: {}", windowNumber);
    }

    public static void switchToParentWindow(WebDriver driver) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(windows.get(0));
        Loggers.log.info("Switch to main window");
    }



}
