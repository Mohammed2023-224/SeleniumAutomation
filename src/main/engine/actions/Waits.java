package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Waits {

    public void implicitWait(WebDriver driver, int time){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(time));
        Loggers.log.info("Add implicit wait by {} seconds", time);
    }

    public WebDriverWait explicitWait(WebDriver driver, int time){
        WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(time));
        return wait;
    }

    public FluentWait fluentWait(WebDriver driver){
        FluentWait wait=new FluentWait(driver);
        return wait;
    }
}
