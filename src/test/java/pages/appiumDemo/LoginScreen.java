package pages.appiumDemo;

import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginScreen {
    private AndroidDriver driver;

    public LoginScreen(AndroidDriver driver){
        this.driver=driver;
    }

    By screenTitle=By.xpath("//android.widget.TextView[@text=\"Login Page\"]");
    By emailField=By.id("com.skill2lead.appiumdemo:id/Et4");
    By passField=By.id("com.skill2lead.appiumdemo:id/Et5");
    By adminField=By.id("com.skill2lead.appiumdemo:id/Edt_admin");
    By submitBtn=By.id("com.skill2lead.appiumdemo:id/Btn3");
    By submitAdminBtn=By.id("com.skill2lead.appiumdemo:id/Btn_admin_sub");
    By previewLine=By.id("com.skill2lead.appiumdemo:id/Tv_admin");
    By enterAdminTitle=By.xpath("//android.widget.TextView[@text=\"Enter Admin\"]");

    private void waitTitleScreen(){
        Waits.waitToBeVisible(screenTitle);
    }
    private void waitAdminTitleScreen(){
        Waits.waitToBeVisible(enterAdminTitle);
    }

    public void typeEmail(){
        waitTitleScreen();
        ElementActions.typeInElement(driver,emailField,"admin@gmail.com");
    }

    public void typePassword(){
        ElementActions.typeInElement(driver,passField,"admin123");
    }

    public void typeAdmin(){
        waitAdminTitleScreen();
        ElementActions.typeInElement(driver,adminField,"admin123");
    }

    public void clickSubmitButton(){
        ElementActions.clickElement(driver,submitBtn);
    }

    public void clickSubmitAdminButton(){
        ElementActions.clickElement(driver,submitAdminBtn);
    }

    public void assertPreview(){
        HardAssertions.assertTru(()-> ElementActions.getText(driver,previewLine).contains("admin123"),
                "Assert that preview contains the correct text");
    }

}
