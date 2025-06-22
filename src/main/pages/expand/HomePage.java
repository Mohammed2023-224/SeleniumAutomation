package pages.expand;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import engine.constants.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
        public WebDriver driver;

        private By link(String linkName){
            return By.xpath("//a[text()='"+linkName+"']");
        }
        public HomePage(WebDriver driver){
            this.driver=driver;
        }

    public void navigateHomePage(){
        BrowserActions.navigateTo(driver, Constants.url);
    }

    public void clickOnSubLink(String linkName){
        ElementActions.clickElement(driver,link(linkName));
    }
}
