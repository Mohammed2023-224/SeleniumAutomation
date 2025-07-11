package pages.expand;

import engine.actions.ElementActions;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WebInputs extends HomePage{

    private By inputNumber= By.name("input-number");
    private By inputText= By.name("input-text");
    private By inputPass= By.name("input-password");
    private By inputDate= By.name("input-date");
    private By displayInputsBtn=By.id("btn-display-inputs");
    private By clearInputsBtn=By.id("btn-clear-inputs");
    public WebInputs(WebDriver driver) {
        super(driver);
    }

    public void typeInputNum(String num){
        ElementActions.scrollToElement(driver,inputNumber);
        ElementActions.typeInElement(driver,inputNumber,num);
        Loggers.addInfoAndAllureStep("Typing in input numbers field: "+num);
    }

    public void typeInputText(String text){
        ElementActions.scrollToElement(driver,inputText);
        ElementActions.typeInElement(driver,inputText,text);
        Loggers.addInfoAndAllureStep("Typing in input text field: "+text);
    }

    public void typeInputPass(String pass){
        ElementActions.scrollToElement(driver,inputPass);
        ElementActions.typeInElement(driver,inputPass,pass);
        Loggers.addInfoAndAllureStep("Typing in input pass field: "+pass);
    }
    public void typeInputDate(String date){
        ElementActions.scrollToElement(driver,inputDate);
        ElementActions.typeInElement(driver,inputDate,date);
        Loggers.addInfoAndAllureStep("Typing in input date field: "+date);
    }

    public void clickClearBtn(){
        ElementActions.scrollToElement(driver,clearInputsBtn);
        ElementActions.clickUsingJavaScript(driver,clearInputsBtn);
        Loggers.addInfoAndAllureStep("Click clear inputs button");
    }

    public void clickDisplayBtn(){
        ElementActions.scrollToElement(driver,displayInputsBtn);
        ElementActions.clickUsingJavaScript(driver,displayInputsBtn);
        Loggers.addInfoAndAllureStep("Click display inputs button");
    }

}
