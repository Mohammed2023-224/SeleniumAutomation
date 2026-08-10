package pages.appiumDemo;

import engine.actions.android.AndroidActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ScrollViewScreen {
    private AndroidDriver driver;

    public ScrollViewScreen(AndroidDriver driver){
        this.driver=driver;
    }
    By screenTitle=By.xpath("//android.widget.TextView[@text=\"ScrollView\"]");
    By alertTitle=By.id("com.skill2lead.appiumdemo:id/alertTitle");
    By yesBtn=By.id("android:id/button1");
    private By buttonsLocator(String number){
        return By.xpath("//android.widget.Button[@text=\"BUTTON"+number+"\"]");
    }

    private void waitTitleScreen(){
        Waits.waitToBeVisible(screenTitle);
    }

    public void scrollToElement(){
        waitTitleScreen();
        AndroidActions.scrollToElement(driver,buttonsLocator("14"));
    }

    public void clickElement(){
        ElementActions.clickElement(driver,buttonsLocator("14"));
    }

    public void handleAlert(){
        Waits.waitToBeVisible(alertTitle);
        ElementActions.clickElement(driver,yesBtn);

    }

}
