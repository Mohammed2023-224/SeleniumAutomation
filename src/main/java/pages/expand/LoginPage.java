package pages.expand;

import engine.actions.ElementActions;
import engine.actions.JSActions;
import engine.actions.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class LoginPage extends HomePage{
    private By userName= By.id("username");
    private By password= By.id("password");
    private By button = By.xpath("//button[text()='Login']");
    private By registerButton = By.xpath("//button[text()='Register']");
    private By successLogin= By.id("username");
    private By registerLink= By.xpath("//a[contains(@href,'register')]");
    private By conPass= By.id("confirmPassword");
    private By logoutLink= By.xpath("//a[contains(@href,'logout')]");
    private By failLogin= By.xpath("//b[contains(text(),'invalid!')]");
    private By logoutAssertion=By.xpath("//b[text()='You logged out of the secure area!']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void typeUserName(String num){
        Waits.waitToBeClickable(driver,userName);
        ElementActions.scrollToElement(driver,userName);
        ElementActions.typeInElement(driver,userName,num);
    }
    public void typePassword(String num){
        ElementActions.scrollToElement(driver,password);
        ElementActions.typeInElement(driver,password,num);
    }
    public void typeConPassword(String num){
        ElementActions.scrollToElement(driver,conPass);
        ElementActions.typeInElement(driver,conPass,num);
    }

    public void loginCycle(String userName, String pass,String assertion){
        typeUserName(userName);
        typePassword(pass);
        clickLogin();
        assertLoginStatus(assertion);
    }
    public void clickLogOut(){
        ElementActions.scrollToElement(driver,logoutLink);
        JSActions.clickUsingJavaScript(driver,logoutLink);
        Waits.waitToBeVisible(driver,logoutAssertion);
    }

    public void clickLogin(){
        ElementActions.scrollToElement(driver, button);
        JSActions.clickUsingJavaScript(driver, button);
    }
    public void clickRegisterLink(){
        ElementActions.scrollToElement(driver,registerLink);
        JSActions.clickUsingJavaScript(driver,registerLink);
    }
    public void clickRegister(){
        ElementActions.scrollToElement(driver,registerButton);
        JSActions.clickUsingJavaScript(driver,registerButton);
    }


    public void assertLoginStatus(String assertionType){
        if(assertionType.equalsIgnoreCase("pass")) {
            Waits.fluentWaitShortTime(driver).until(ExpectedConditions.not(ExpectedConditions.textToBe(successLogin, "")));
            Assert.assertTrue(ElementActions.checkIfElementVisible(driver, successLogin));
            clickLogOut();
        }
        else {
            Waits.waitToBeVisible(driver,failLogin);
            Assert.assertTrue(ElementActions.checkIfElementVisible(driver,failLogin));
        }
    }


}
