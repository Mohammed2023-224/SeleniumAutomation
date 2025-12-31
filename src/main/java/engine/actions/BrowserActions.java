package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class BrowserActions {
    public static void navigateTo(WebDriver driver, String url) {
        String logs="Navigated to url: "+ url;
        driver.navigate().to(url);
       Loggers.getLogger().info(logs);
    }
    public static void navigateBack(WebDriver driver) {
        String logs="Navigated to last page";
        driver.navigate().back();
       Loggers.getLogger().info(logs);
    }

    public static void navigateForward(WebDriver driver) {
        String logs="Navigated forward";
        driver.navigate().forward();
       Loggers.getLogger().info(logs);
    }

    public static void switchIframe(WebDriver driver, By frameLocator) {
        String log="Switch to iframe located at: "+ frameLocator;
        driver.switchTo().frame(driver.findElement(frameLocator));
       Loggers.getLogger().info(log);
    }

    public static void switchParentFrame(WebDriver driver) {
        String log="Switch to main frame";
        driver.switchTo().parentFrame();
       Loggers.getLogger().info(log);
    }

    public static void switchToWindowByIndex(WebDriver driver, int windowNumber) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windowNumber >= 0 && windowNumber < windows.size()) {
            driver.switchTo().window(windows.get(windowNumber));
        }
        String log="Switch to windows number: "+ windowNumber;
       Loggers.getLogger().info(log);
    }

    public static void switchToWindowByTitle(WebDriver driver, String title) {
        String log="";
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        for(String t: windows){
            driver.switchTo().window(t);
            if(driver.getTitle().equalsIgnoreCase(title)){
                log="Switch to windows titled: "+ title;
                break;
            }
        }
        if(log.isEmpty()){
            log="No windows was found with title "+ title+" so switched to first window ";
            switchToFirstWindow(driver);
        }
       Loggers.getLogger().info(log);
    }

    public static void switchToWindowByURL(WebDriver driver, String url) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        for(String u: windows){
            driver.switchTo().window(u);
            if(driver.getCurrentUrl().equalsIgnoreCase(url)){
                break;
            }
        }
        String log="Switch to windows url: "+ url;
       Loggers.getLogger().info(log);
    }

    public static void switchToFirstWindow(WebDriver driver) {
        String log="Switch to main window";
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(windows.get(0));
       Loggers.getLogger().info(log);
    }

    public static void acceptAlert(WebDriver driver){
        String log="Accept existing alert";
        driver.switchTo().alert().accept();
        Loggers.getLogger().info(log);
    }
}
