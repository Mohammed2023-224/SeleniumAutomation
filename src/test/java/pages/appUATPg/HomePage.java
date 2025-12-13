package pages.appUATPg;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import engine.constants.Constants;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
WebDriver driver;

public HomePage (WebDriver driver){
    this.driver=driver;
}

public By pages(String tabName){
    return By.xpath("//h5[contains(text(),'"+tabName+"')]//parent::div//parent::div//a");
}

@Step("Navigate to home page")
    public void navigateHomePage(){
    BrowserActions.navigateTo(driver, Constants.testPlayGroundMainPage);
}

    @Step("click page")
    public void clickPage(String tab){
        ElementActions.clickElement(driver,pages(tab));
    }

}
