package engine.actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class Helpers {
;

    public static JavascriptExecutor initiateJSExecutor(WebDriver driver) {
            return (JavascriptExecutor) driver;
    }

    public static Actions seleniumActions(WebDriver driver) {
           return new Actions(driver);

    }

}
