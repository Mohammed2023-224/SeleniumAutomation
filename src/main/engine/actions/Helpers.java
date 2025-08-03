package engine.actions;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class Helpers {
    public  Actions acts;
    static JavascriptExecutor js;

    public  JavascriptExecutor initiateJSExecutor(WebDriver driver) {
        if(!(js ==null)){
            return js;
        }else {
            return (JavascriptExecutor) driver;
        }
    }

    public  Actions seleniumActions(WebDriver driver) {
        if (acts == null) {
            acts = new Actions(driver);
        }
        return acts;
    }

}
