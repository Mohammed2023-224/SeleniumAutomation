package engine.actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class Helpers {

    public static JavascriptExecutor initiateJSExecutor(WebDriver driver){
        return (JavascriptExecutor) driver;
    }

}
