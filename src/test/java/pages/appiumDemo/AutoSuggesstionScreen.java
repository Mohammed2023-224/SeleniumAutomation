package pages.appiumDemo;

import engine.actions.android.AndroidActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class AutoSuggesstionScreen {
    private AndroidDriver driver;

    public AutoSuggesstionScreen(AndroidDriver driver){
        this.driver=driver;
    }

    By textFiled=By.id("com.skill2lead.appiumdemo:id/multiAutoCompleteTextView");
    By submitBtn=By.id("com.skill2lead.appiumdemo:id/btn_submit");
    By previewLine=By.id("com.skill2lead.appiumdemo:id/tv_value");

    public void typeText(String text){
        Waits.waitToBeVisible(driver,textFiled);
        ElementActions.typeInElement(driver,textFiled,text);
        AndroidActions.touchCoordinates(driver,204,521);
    }

    public void clickSubmitButton(){
        ElementActions.clickElement(driver,submitBtn);
    }

    public void assertPreview(String text){
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewLine).contains(text),
                "Assert that preview contains the correct text");

    }

}
