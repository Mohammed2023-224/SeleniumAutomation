package engine.actions;

import engine.constants.Constants;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {

    public static void implicitWait(WebDriver driver, int time){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
        Loggers.log.info("Add implicit wait by {} seconds", time);
    }

    public static WebDriverWait explicitWait(WebDriver driver){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(Constants.time));
        Loggers.log.info("initialize explicit wait");
        return wait;
    }

    public static FluentWait fluentWait(WebDriver driver){
        FluentWait wait=new FluentWait(driver);
        return wait;
    }

    public static void waitToBeVisible(WebDriver driver, By locator ){
        if(ElementActions.checkIfElementVisible(driver,locator)){
            Loggers.log.info("element located at {} is visible", locator);
        }
        else {
            explicitWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
            Loggers.log.info("wait for element located at {} to be visible for {}", locator,Constants.time);
        }
    }

    public static void waitToBeInvisible(WebDriver driver, By locator ){
        if(!ElementActions.checkIfElementVisible(driver,locator)){
            Loggers.log.info("element located at {} is invisible", locator);
        }
        else {
            explicitWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
            Loggers.log.info("wait for element located at {} to be invisible for {}", locator,Constants.time);
        }
    }


    public static void waitToBeClickable(WebDriver driver, By locator  ){
        if(ElementActions.checkIfElementClickable(driver,locator)){
            Loggers.log.info("element located at {} is clickable", locator);
        }
        else {
            explicitWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
            Loggers.log.info("wait for element located at {} to be clickable for {}", locator,Constants.time);
        }
    }


    public static void waitToExist(WebDriver driver, By locator  ){
        if(ElementActions.checkIfElementExists(driver,locator)){
            Loggers.log.info("element located at {} exists", locator);
        }
        else {
            explicitWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
            Loggers.log.info("wait for element located at {} to exist for {}", locator,Constants.time);
        }
    }


}
