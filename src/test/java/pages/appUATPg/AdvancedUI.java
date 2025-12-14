package pages.appUATPg;

import engine.actions.ElementActions;
import engine.actions.JSActions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class AdvancedUI {
private final WebDriver driver;
    By starRating= By.className("star-rating");
    By textRating= By.id("txt_rating");
    By checkRating= By.id("check_rating");
    By validateRating= By.id("validate_rating");

public AdvancedUI(WebDriver driver){
    this.driver=driver;

}
public void handleAdvancedUIChallenge(){
    String text= getElementContent();
    typeTextRating(text);
    clickCheckRating();
    assertRatingField("Well done!");
}

@Step("Get star rating content")
    private String getElementContent(){
        return JSActions.getPseudoElementContent(driver,starRating,"::after");
    }

    @Step("Type text in star rating: {text}")
    private void typeTextRating(String text){
        ElementActions.typeInElement(driver,textRating,text);
    }

    @Step("click check rating ")
    private void clickCheckRating(){
        ElementActions.clickElement(driver,checkRating);
    }

    @Step("Assert that rating field shows message {message}")
    private void assertRatingField(String message){
        Assert.assertTrue(ElementActions.getText(driver,validateRating).contains(message));
    }

}
