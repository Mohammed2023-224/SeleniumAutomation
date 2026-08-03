package pages.appiumDemo;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

public class LongClickScreen {
    private AppiumDriver driver;
    By alertTitle=By.id("android:id/alertTitle");
    By submit=By.id("android:id/button1");
    By emailField=By.id("com.skill2lead.appiumdemo:id/et_email");


    public LongClickScreen(AppiumDriver driver){
        this.driver=driver;
    }

    public void typeEmailInAlert(){
       Waits.waitToBeVisible(alertTitle);
        ElementActions.typeInElement(driver,emailField,"sasa@sasa");
        ElementActions.clickElement(driver,submit);
    }

}
