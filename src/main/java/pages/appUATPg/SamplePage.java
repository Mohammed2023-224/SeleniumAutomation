package pages.appUATPg;

import engine.actions.ElementActions;
import engine.actions.Waits;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SamplePage {
private final WebDriver driver;
    By quantity= By.id("quantity");
    By flavor= By.id("select_flavor");
    By added= By.id("added_message");
    By submitButton= By.id("submit_button");
    By warning=By.className("modal-body");
    By closeWarning= By.xpath("//button[@class='btn btn-warning']");
    By userName= By.id("user");
    By pass= By.id("password");
    By passByName= By.name("password");
    By login= By.id("login");
    By rememberMe= By.xpath("//input[@type='checkbox']");
    By registerLink= By.tagName("a");
    By assertion= By.tagName("h1");
    By firstName= By.name("first_name");
    By lastName= By.name("last_name");
    By email= By.name("email");
    By conPass= By.name("confirm_password");
    By terms= By.name("terms");
    By submit= By.name("submit_button");

public SamplePage(WebDriver driver){
    this.driver=driver;
}
private By options(String option){
    return By.xpath("//input[@value='"+option+"']");
}
public void loginForm(){
    ElementActions.typeInElement(driver,userName,"admin");
    ElementActions.clickElement(driver,login);
    Assert.assertTrue(ElementActions.getElementPropertyJSExecutor(driver,pass,"validationMessage").contains("Please fill out this field."));
    ElementActions.typeInElement(driver,pass,"admin");
        ElementActions.clickElement(driver,rememberMe);
        ElementActions.clickElement(driver,login);
    }
    public void pizzaForm(){
        ElementActions.clickElement(driver,options("SMALL"));
        ElementActions.clickElement(driver,options("BUFFALO"));
        ElementActions.clickElement(driver,options("ONIONS"));
        ElementActions.clickElement(driver,options("green_olive"));
        ElementActions.clickElement(driver,options("tomato"));
        ElementActions.clickElement(driver,submitButton);
        Waits.waitToBeVisible(driver,warning);
        Assert.assertTrue(driver.findElement(warning).getText().contains("1 or more!"));
        ElementActions.clickElement(driver,closeWarning);
        ElementActions.typeInElement(driver,quantity,"5");
    ElementActions.selectDDLOptionText(driver,flavor,"Pepperoni");
        ElementActions.clickElement(driver,submitButton);
        Waits.waitToBeVisible(driver,added);
        Assert.assertTrue(ElementActions.getText(driver,added).contains("Pizza added to the cart!"));
    }

    public void newUser(){
        ElementActions.clickElement(driver,registerLink);
        ElementActions.typeInElement(driver,firstName,"test");
        ElementActions.typeInElement(driver,lastName,"test");
        ElementActions.typeInElement(driver,email,"test");
        ElementActions.typeInElement(driver,passByName,"test");
        ElementActions.typeInElement(driver,conPass,"test");
        ElementActions.clickElement(driver,terms);
        ElementActions.clickElement(driver,submit);
        Assert.assertTrue(ElementActions.getText(driver,assertion).contains("Confirmation"));
        driver.navigate().back();
        driver.navigate().back();
    }
}
