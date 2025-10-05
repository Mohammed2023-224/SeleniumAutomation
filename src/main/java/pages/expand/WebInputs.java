package pages.expand;

import engine.actions.ElementActions;
import engine.actions.Helpers;
import engine.actions.Waits;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class WebInputs extends HomePage{

    private By inputNumber= By.name("input-number");
    private By inputText= By.name("input-text");
    private By inputPass= By.name("input-password");
    private By inputDate= By.name("input-date");
    private By displayInputsBtn=By.id("btn-display-inputs");
    private By clearInputsBtn=By.id("btn-clear-inputs");
    private By outputNumber=By.id("output-number");
    public WebInputs(WebDriver driver) {
        super(driver);
    }

    public void typeInputNum(String num){
        ElementActions.scrollToElement(driver,inputNumber);
        ElementActions.typeInElement(driver,inputNumber,num);
    }

    public void typeInputText(String text){
        ElementActions.scrollToElement(driver,inputText);
        ElementActions.typeInElement(driver,inputText,text);
    }

    public void typeInputPass(String pass){
        ElementActions.scrollToElement(driver,inputPass);
        ElementActions.typeInElement(driver,inputPass,pass);
    }
    public void typeInputDate(String date){
        ElementActions.scrollToElement(driver,inputDate);
        ElementActions.typeInElement(driver,inputDate,date);
    }

    public void clickClearBtn(){
        ElementActions.scrollToElement(driver,clearInputsBtn);
        ElementActions.clickUsingJavaScript(driver,clearInputsBtn);
        Waits.waitToBeInvisible(driver,outputNumber);
        Assert.assertFalse(ElementActions.checkIfElementVisible(driver,outputNumber));
    }

    public void clickDisplayBtn(){
        ElementActions.scrollToElement(driver,displayInputsBtn);
        ElementActions.clickUsingJavaScript(driver,displayInputsBtn);
        Waits.waitToBeVisible(driver,outputNumber);
        Assert.assertTrue(ElementActions.checkIfElementVisible(driver,outputNumber));
    }

    public void assertText(String text){
        Assert.assertEquals(ElementActions.getText(driver,outputNumber),text);
    }

}
