package engine.actions;

import engine.constants.Constants;
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

    public static void implicitWait(WebDriver driver, int time){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
        Loggers.log.info("Add implicit wait by {} seconds", time);
    }

    private static WebDriverWait explicitWaitLongTime(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(Constants.longWaitTime));
        Loggers.log.info("initialize explicit wait for {} sec" ,Constants.longWaitTime);
        return wait;
    }

    private static WebDriverWait explicitWaitShortTime(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(Constants.longWaitTime));
        Loggers.log.info("initialize explicit wait for {} seconds" ,Constants.shortWaitTime);
        return wait;
    }

    public static FluentWait<WebDriver> fluentWaitShortTime(WebDriver driver,int timeout,int polling){
        return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofSeconds(polling))
                    .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class);
    }



    public static void waitToBeVisible(WebDriver driver, By locator ){
        if(ElementActions.checkIfElementVisible(driver,locator)){
            Loggers.log.info("element located at {} is visible", locator);
        }
        else {
            explicitWaitLongTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
            Loggers.log.info("wait for element located at {} to be visible for {}", locator,Constants.longWaitTime);
        }
    }

    public static void waitToBeInvisible(WebDriver driver, By locator ){
        if(!ElementActions.checkIfElementVisible(driver,locator)){
            Loggers.log.info("element located at {} is invisible", locator);
        }
        else {
            explicitWaitLongTime(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
            Loggers.log.info("wait for element located at {} to be invisible for {}", locator,Constants.longWaitTime);
        }
    }


    public static void waitToBeClickable(WebDriver driver, By locator  ){
        if(ElementActions.checkIfElementClickable(driver,locator)){
            Loggers.log.info("element located at {} is clickable", locator);
        }
        else {
            explicitWaitLongTime(driver).until(ExpectedConditions.elementToBeClickable(locator));
            Loggers.log.info("wait for element located at {} to be clickable for {}", locator,Constants.longWaitTime);
        }
    }


    public static void waitToExist(WebDriver driver, By locator){
        if(ElementActions.checkIfElementExists(driver,locator)){
            Loggers.log.info("element located at {} exists", locator);
        }
        else {
            explicitWaitLongTime(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
            Loggers.log.info("wait for element located at {} to exist for {}", locator,Constants.longWaitTime);
        }
    }

    public static void waitElementToContainText(WebDriver driver, By locator,String text){
        explicitWaitLongTime(driver).until(x-> x.findElement(locator).getText().contains(text));
        Loggers.log.info("wait for element located at {} to contain text {} for {}", locator,text,Constants.longWaitTime);
    }


    public static boolean waitForFileToBeDownloaded(WebDriver driver,String path){
        File file = new File(path);
        try{
            explicitWaitLongTime(driver).until(x ->file.exists() && file.canRead());
            Loggers.log.info("Waited for file [{}] to appear for [{}]",path,Constants.longWaitTime);
        }
        catch (Exception e){
            Loggers.log.info("The file [{}] didn't appear after [{}] ",path,Constants.longWaitTime);
        }
        return file.exists();
    }

}
