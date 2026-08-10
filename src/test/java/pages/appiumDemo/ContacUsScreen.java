package pages.appiumDemo;

import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ContacUsScreen {
    private AndroidDriver driver;

    public ContacUsScreen(AndroidDriver driver){
        this.driver=driver;
    }

    By screenTitle=By.xpath("//android.widget.TextView[@text=\"Contact Us form\"]");
    By nameField=By.id("com.skill2lead.appiumdemo:id/Et2");
    By emailField=By.id("com.skill2lead.appiumdemo:id/Et3");
    By addressField=By.id("com.skill2lead.appiumdemo:id/Et6");
    By mobileField=By.id("com.skill2lead.appiumdemo:id/Et7");
    By submitBtn=By.id("com.skill2lead.appiumdemo:id/Btn2");
    By previewNameLine=By.id("com.skill2lead.appiumdemo:id/Tv2");
    By previewEmailLine=By.id("com.skill2lead.appiumdemo:id/Tv7");
    By previewPassLine=By.id("com.skill2lead.appiumdemo:id/Tv5");
    By previewMobileLine=By.id("com.skill2lead.appiumdemo:id/Tv6");

    private void waitTitleScreen(){
        Waits.waitToBeVisible(screenTitle);
    }

    public void typeName(String text){
        waitTitleScreen();
        ElementActions.typeInElement(driver,nameField,text);
    }

    public void typeEmail(String text){
        waitTitleScreen();
        ElementActions.typeInElement(driver,emailField,text);
    }

    public void typeAddress(String text){
        waitTitleScreen();
        ElementActions.typeInElement(driver,addressField,text);
    }

    public void typeMobile(String text){
        waitTitleScreen();
        ElementActions.typeInElement(driver,mobileField,text);
    }

    public void clickSubmitButton(){
        waitTitleScreen();
        ElementActions.clickElement(driver,submitBtn);
    }

    public void assertPreview(String name,String email,String pass,String mobile){
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewNameLine).contains(name),
                "Assert that preview contains the correct name; "+name);
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewEmailLine).contains(email),
                "Assert that preview contains the correct email: "+email);
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewPassLine).contains(pass),
                "Assert that preview contains the correct address: "+pass);
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewMobileLine).contains(mobile),
                "Assert that preview contains the correct mobile : "+mobile);
    }

}
