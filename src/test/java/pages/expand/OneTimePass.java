package pages.expand;

import engine.actions.ElementActions;
import engine.actions.JSActions;
import engine.actions.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class OneTimePass extends HomePage{

    private By otpCode= By.xpath("//li[contains(text(),'OTP Code')]//b");
    private By emailField=By.id("email");
    private By otpField=By.id("otp");
    private By welcomeMessage=By.id("username");
    private By sendOTPButton=By.id("btn-send-otp");
    private By verifyOTPBN=By.id("btn-send-verify");
    private By otpAssertionMessage=By.id("otp-message");

    public OneTimePass(WebDriver driver) {
        super(driver);
    }
    public void typeEmail(String num){
        ElementActions.clearField(driver,emailField);
        ElementActions.typeInElement(driver,emailField,num);
    }
    public void typeOTP(String num){
        ElementActions.typeInElement(driver,otpField,num);
    }

    public String getOTP(){
        return ElementActions.getText(driver,otpCode);
    }

    public void  assertWelcomeMessage(){
        Waits.waitToBeVisible(driver,welcomeMessage);
        Assert.assertTrue(ElementActions.checkIfElementVisible(driver,welcomeMessage));
    }

    public void  assertFail(){
        Waits.waitToBeVisible(driver,otpAssertionMessage);
        Assert.assertTrue(ElementActions.checkIfElementVisible(driver,otpAssertionMessage));
    }


    public void clickSendOTP(){
        JSActions.clickUsingJavaScript(driver,sendOTPButton);
        Waits.waitToBeInvisible(driver,sendOTPButton);
    }
    public void clickVerifyOTP(){
        JSActions.clickUsingJavaScript(driver,verifyOTPBN);
//        Waits.waitToBeInvisible(driver,verifyOTPBN);
    }
}
