package pages.appiumDemo;

import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class EnterSomeValueScreen {
    private AppiumDriver driver;

    public EnterSomeValueScreen(AppiumDriver driver){
        this.driver=driver;
    }

    By screenTitle=By.xpath("//android.widget.TextView[@text=\"Enter some Value\"]");
    By textFiled=By.id("com.skill2lead.appiumdemo:id/Et1");
    By submitBtn=By.id("com.skill2lead.appiumdemo:id/Btn1");
    By previewLine=By.id("com.skill2lead.appiumdemo:id/Tv1");

    private void waitTitleScreen(){
        Waits.waitToBeVisible(screenTitle);
    }

    public void typeText(String text){
        waitTitleScreen();
        ElementActions.typeInElement(driver,textFiled,text);
    }

    public void clickSubmitButton(){
        waitTitleScreen();
        ElementActions.clickElement(driver,submitBtn);
    }

    public void assertPreview(String text){
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewLine).contains(text),
                "Assert that preview contains the correct text");
    }

}
