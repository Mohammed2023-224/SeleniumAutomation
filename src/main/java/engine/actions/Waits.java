package engine.actions;

import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class Waits {
private Waits(){}
    public static void implicitWait(WebDriver driver, int time){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
     Loggers.logInfo("Add implicit wait by "+ time+" seconds");
    }
    public static void waitForAlert(WebDriver driver,int time){
        explicitWaitShortTime(driver).until(ExpectedConditions.alertIsPresent());
     Loggers.logInfo("Waited for alert to be present for ["+ time+"]");
    }
    public static WebDriverWait explicitWaitLongTime(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(FrameworkConfigs.shortWait()));
     Loggers.logInfo(" explicit wait for "+FrameworkConfigs.shortWait()+" sec" );
        return wait;
    }

    public static WebDriverWait explicitWaitShortTime(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(FrameworkConfigs.shortWait()));
     Loggers.logInfo(" explicit wait for "+FrameworkConfigs.shortWait()+" seconds" );
        return wait;
    }

    public static FluentWait<WebDriver> fluentWaitShortTime(WebDriver driver){
        return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(FrameworkConfigs.shortWait())).pollingEvery(Duration.ofSeconds(1))
                    .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class);
    }
    public static FluentWait<WebDriver> fluentWaitLongTime(WebDriver driver){
        return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(FrameworkConfigs.longWait())).pollingEvery(Duration.ofSeconds(1))
                    .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class);
    }



    public static void waitToBeVisible(WebDriver driver, By locator ){
            explicitWaitLongTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
         Loggers.logInfo("wait for element located at "+locator+" to be visible for "+FrameworkConfigs.longWait());

    }

    public static void waitToBeInvisible(WebDriver driver, By locator ){
            explicitWaitLongTime(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
         Loggers.logInfo("wait for element located at "+locator+" to be invisible for "+ FrameworkConfigs.longWait());

    }


    public static void waitToBeClickable(WebDriver driver, By locator  ) {
            explicitWaitLongTime(driver).until(ExpectedConditions.elementToBeClickable(locator));
            Loggers.logInfo("wait for element located at "+locator+" to be clickable for " + FrameworkConfigs.longWait());
    }


    public static void waitToExist(WebDriver driver, By locator){
            explicitWaitLongTime(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
         Loggers.logInfo("wait for element located at "+locator+" to exist for "+ FrameworkConfigs.longWait());

    }

    public static void waitElementToContainText(WebDriver driver, By locator,String text){
        explicitWaitLongTime(driver).until(x-> x.findElement(locator).getText().contains(text));
     Loggers.logInfo("wait for element located at "+locator+" to contain text "+text+" for "+ FrameworkConfigs.longWait());
    }

    public static void waitForTextToChange(WebDriver driver, By locator,String text){
        fluentWaitShortTime(driver).until(x-> !x.findElement(locator).getText().contains(text));
     Loggers.logInfo("wait for element located at "+locator+" to not have text "+text+" for "+ FrameworkConfigs.longWait());
    }


    public static void waitForFileToBeDownloaded(WebDriver driver,String path){
        File file = new File(path);
            explicitWaitLongTime(driver).until(x ->file.exists() && file.canRead());
         Loggers.logInfo("Waited for file ["+path+"] to appear for "+ FrameworkConfigs.longWait());
    }

}
