package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.ArrayList;
import java.util.Objects;

public class BrowserActions {
    private BrowserActions(){}
    public static void navigateTo(WebDriver driver, String url) {
        driver.navigate().to(url);
       Loggers.logInfo("Navigated to url: "+ url);
    }
    public static void navigateBack(WebDriver driver) {
        driver.navigate().back();
       Loggers.logInfo("Navigated back to last page");
    }

    public static void navigateForward(WebDriver driver) {
        driver.navigate().forward();
       Loggers.logInfo("Navigated forward");
    }

    public static void switchIframe(WebDriver driver, By frameLocator) {
        driver.switchTo().frame(driver.findElement(frameLocator));
       Loggers.logInfo("Switch to iframe located at: "+ frameLocator);
    }

    public static void switchParentFrame(WebDriver driver) {
        driver.switchTo().parentFrame();
       Loggers.logInfo("Switch to main frame");
    }

    public static void switchToWindowByIndex(WebDriver driver, int windowNumber) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windowNumber >= 0 && windowNumber < windows.size()) {
            driver.switchTo().window(windows.get(windowNumber));
        }
       Loggers.logInfo("Switch to windows number: "+ windowNumber);
    }

    public static void switchToWindowByTitle(WebDriver driver, String title) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        for(String t: windows){
            driver.switchTo().window(t);
            try {
                if(Objects.requireNonNull(driver.getTitle()).equalsIgnoreCase(title)){
                    Loggers.logInfo("Switch to windows titled: "+ title);
                    break;
                }
            } catch (NullPointerException e) {
                Loggers.logInfo("No windows was found with title "+ title+" so switched to first window");
                switchToFirstWindow(driver);
            }
        }
    }

    public static void switchToWindowByURL(WebDriver driver, String url) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        for(String u: windows){
            driver.switchTo().window(u);
            try {
                if (Objects.requireNonNull(driver.getCurrentUrl()).equalsIgnoreCase(url)) {
                    break;
                }
            } catch (NullPointerException e) {
                throw new NullPointerException();
            }
        }
       Loggers.logInfo("Switch to windows url: "+ url);
    }

    public static void switchToFirstWindow(WebDriver driver) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(windows.getFirst());
       Loggers.logInfo("Switch to main window");
    }

    public static void acceptAlert(WebDriver driver){
        driver.switchTo().alert().accept();
        Loggers.logInfo("Accept existing alert");
    }
}