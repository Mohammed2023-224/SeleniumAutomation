package engine.actions;


import engine.constants.FrameworkConfigs;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitsManager {
    private static final ThreadLocal<WebDriverWait> longWait = new ThreadLocal<>();
    private static final ThreadLocal<FluentWait<WebDriver>> fluentLongWait = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> shortWait = new ThreadLocal<>();
    private static final ThreadLocal<FluentWait<WebDriver>> fluentShortWait = new ThreadLocal<>();

    public static void setWaits(WebDriver driver) {
        longWait.set(new WebDriverWait(driver,
                Duration.ofSeconds(FrameworkConfigs.longWait())));

        shortWait.set(new WebDriverWait(driver,
                Duration.ofSeconds(FrameworkConfigs.shortWait())));

        fluentLongWait.set(new FluentWait<>(driver).withTimeout(Duration.ofSeconds(FrameworkConfigs.longWait())).pollingEvery(Duration.ofSeconds(1))
                .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class));

        fluentShortWait.set(new FluentWait<>(driver).withTimeout(Duration.ofSeconds(FrameworkConfigs.shortWait())).pollingEvery(Duration.ofSeconds(1))
                .ignoring(TimeoutException.class).ignoring(StaleElementReferenceException.class));
    }

    public static WebDriverWait getLongWait(){
        return longWait.get();
    }

    public static WebDriverWait getShortWait(){
        return shortWait.get();
    }

    public static FluentWait<WebDriver> getLongFluentWait(){
        return fluentLongWait.get();
    }

    public static FluentWait<WebDriver> getShortFluentWait(){
        return fluentShortWait.get();
    }


    public static void removeWaits(){
        longWait.remove();
        shortWait.remove();
        fluentLongWait.remove();
        fluentShortWait.remove();
    }


}
