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
     Loggers.getLogger().info("Add implicit wait by {} seconds", time);
    }
    public static void waitForAlert(WebDriver driver,int time){
        explicitWaitShortTime(driver).until(ExpectedConditions.alertIsPresent());
     Loggers.getLogger().info("Waited for alert to be present for [{}]",time);
    }
    public static WebDriverWait explicitWaitLongTime(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(FrameworkConfigs.shortWait()));
     Loggers.getLogger().info(" explicit wait for {} sec" , FrameworkConfigs.shortWait());
        return wait;
    }

    public static WebDriverWait explicitWaitShortTime(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(FrameworkConfigs.shortWait()));
     Loggers.getLogger().info(" explicit wait for {} seconds" , FrameworkConfigs.shortWait());
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
         Loggers.getLogger().info("wait for element located at {}} to be visible for {}", locator,FrameworkConfigs.longWait());

    }

    public static void waitToBeInvisible(WebDriver driver, By locator ){
            explicitWaitLongTime(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
         Loggers.getLogger().info("wait for element located at {} to be invisible for {}", locator, FrameworkConfigs.longWait());

    }


    public static void waitToBeClickable(WebDriver driver, By locator  ) {
            explicitWaitLongTime(driver).until(ExpectedConditions.elementToBeClickable(locator));
            Loggers.getLogger().info("wait for element located at {} to be clickable for {}" ,locator, FrameworkConfigs.longWait());
    }


    public static void waitToExist(WebDriver driver, By locator){
            explicitWaitLongTime(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
         Loggers.getLogger().info("wait for element located at {} to exist for {}", locator, FrameworkConfigs.longWait());

    }

    public static void waitElementToContainText(WebDriver driver, By locator,String text){
        explicitWaitLongTime(driver).until(x-> x.findElement(locator).getText().contains(text));
     Loggers.getLogger().info("wait for element located at {} to contain text {} for {}", locator,text, FrameworkConfigs.longWait());
    }


    public static void waitForFileToBeDownloaded(WebDriver driver,String path){
        File file = new File(path);
            explicitWaitLongTime(driver).until(x ->file.exists() && file.canRead());
         Loggers.getLogger().info("Waited for file [{}] to appear for [{}]",path, FrameworkConfigs.longWait());
    }

}
