package engine.actions;

import engine.constants.FrameworkConfigs;
import engine.enums.WaitTypes;
import engine.reporters.Loggers;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.function.Function;

public class Waits {
    private Waits() {
    }


    public static void implicitWait(WebDriver driver, int time) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
        Loggers.logInfo("Add implicit wait by " + time + " seconds");
    }



    public static WebDriverWait explicitWaitLongTime(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(FrameworkConfigs.longWait()));
    }

    public static FluentWait<WebDriver> fluentWaitShortTime(WebDriver driver) {
        return new FluentWait<>(driver).withTimeout(Duration.ofSeconds(FrameworkConfigs.shortWait())).pollingEvery(Duration.ofSeconds(1))
                .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class);
    }
    /// ////////////////////////////////////////////////////////////////////////////////////////////////////
         public static void waitForAlert( int time) {
             Waits.customWait(WaitTypes.ShortWait,"Wait for alert",ExpectedConditions.alertIsPresent());
             Loggers.logInfo("Waited for alert to be present for [" + time + "]");
         }

    public static void waitToBeVisible(WebDriver driver, By locator) {
        explicitWaitLongTime(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        Loggers.logInfo("wait for element located at " + locator + " to be visible for " + FrameworkConfigs.longWait());

    }

    public static void waitToBeInvisible(WebDriver driver, By locator) {
        explicitWaitLongTime(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
        Loggers.logInfo("wait for element located at " + locator + " to be invisible for " + FrameworkConfigs.longWait());

    }

    public static void waitToBeClickable( By locator) {
        customWait(WaitTypes.ShortWait,"wait for element: "+locator+" to be clickable for "+ FrameworkConfigs.shortWait()
                ,ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitToBeVisible( By locator) {
        customWait(WaitTypes.ShortWait,"wait for element: "+locator+" to be visible for "+ FrameworkConfigs.shortWait()
                ,ExpectedConditions.visibilityOfElementLocated(locator));
    }

        public static void waitElementToContainText(By locator, String text) {
        WaitsManager.getShortWait().until(x -> x.findElement(locator).getText().contains(text));
        Loggers.logInfo("wait for element located at " + locator + " to contain text " + text + " for " + FrameworkConfigs.longWait());
    }

    public static void waitForTextToChange(By locator, String text) {
        WaitsManager.getShortWait().until(x -> !x.findElement(locator).getText().contains(text));
        Loggers.logInfo("wait for element located at " + locator + " to not have text " + text + " for " + FrameworkConfigs.longWait());
    }

    public static void waitForFileToBeDownloaded( String path) {
        File file = new File(path);
        customWait(WaitTypes.ShortWait,"Wait for file [" + path + "] to appear for " + FrameworkConfigs.longWait()
                ,x -> file.exists() && file.canRead());
    }


    public static  <T> T customWait(
            WaitTypes waitType,
            String log,
            Function<? super WebDriver, T> condition) {
        waitSelector(waitType).until(condition);
        Loggers.logInfo(log);
        return null;
    }

    public static <T> T customWait(WebDriver driver,
                           String log,
                           Function<? super WebDriver, T> condition, int time) {
        new WebDriverWait(driver, Duration.ofSeconds(time)).until(condition);
        Loggers.logInfo(log);
        return null;
    }

    public static void customFluentWait(WebDriver driver,
                                 String log,
                                 Function<? super WebDriver, T> condition, int time) {
        new FluentWait<>(driver).withTimeout(Duration.ofSeconds(time)).pollingEvery(Duration.ofSeconds(1))
                .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class).until(condition);
        Loggers.logInfo(log);
    }


    private static FluentWait<WebDriver> waitSelector(WaitTypes waitType) {
        switch (waitType) {
            case ShortWait -> {
                return WaitsManager.getShortWait();
            }
            case LongWait -> {
                return WaitsManager.getLongWait();
            }
            case FluentWaitLong -> {
                return WaitsManager.getLongFluentWait();
            }
            case FluentWaitShort -> {
                return WaitsManager.getShortFluentWait();
            }
        }

        return null;
    }
}

