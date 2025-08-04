package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class BrowserActions {
    public static void navigateTo(WebDriver driver, String url) {
        String logs="Navigated to url: {}"+ url;
        driver.navigate().to(url);
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }
    public static void navigateBack(WebDriver driver) {
        String logs="Navigated to last page";
        driver.navigate().back();
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }

    public static void navigateForward(WebDriver driver) {
        String logs="Navigated forward";
        driver.navigate().forward();
        Loggers.getInstance().addInfoAndAllureStep(logs);
    }

    public static void switchIframe(WebDriver driver, By frameLocator) {
        String log="Switch to iframe located at: {}"+ frameLocator;
        driver.switchTo().frame(driver.findElement(frameLocator));
        Loggers.getInstance().addInfoAndAllureStep(log);
    }

    public static void switchParentFrame(WebDriver driver) {
        String log="Switch to main frame";
        driver.switchTo().parentFrame();
        Loggers.getInstance().addInfoAndAllureStep(log);
    }

    public static void switchToWindowByIndex(WebDriver driver, int windowNumber) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (windowNumber >= 0 && windowNumber < windows.size()) {
            driver.switchTo().window(windows.get(windowNumber));
        }
        String log="Switch to windows number: "+ windowNumber;
        Loggers.getInstance().addInfoAndAllureStep(log);
    }

    public static void switchToWindowByTitle(WebDriver driver, String title) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        for(String t: windows){
            driver.switchTo().window(t);
            if(driver.getTitle().equalsIgnoreCase(title)){
                break;
            }
        }
        String log="Switch to windows titled: "+ title;
        Loggers.getInstance().addInfoAndAllureStep(log);
    }

    public static void switchToWindowByURL(WebDriver driver, String url) {
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
        for(String u: windows){
            driver.switchTo().window(u);
            if(driver.getCurrentUrl().equalsIgnoreCase(url)){
                break;
            }
        }
        String log="Switch to windows titled: "+ url;
        Loggers.getInstance().addInfoAndAllureStep(log);
    }

    public static void switchToParentWindow(WebDriver driver) {
        String log="Switch to main window";
        ArrayList<String> windows = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(windows.get(0));
        Loggers.getInstance().addInfoAndAllureStep(log);
    }

    public static void acceptAlert(WebDriver driver){
        driver.switchTo().alert().accept();
        Loggers.getInstance().getInstance().log.info("Accept url");
    }
}
