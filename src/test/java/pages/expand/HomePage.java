package pages.expand;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import engine.actions.JSActions;
import engine.utils.PropertyReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class HomePage {
         WebDriver driver;

        private By link(String linkName){
            return By.xpath("//a[text()='"+linkName+"']");
        }
        public HomePage(WebDriver driver){
            this.driver=driver;
        }

    public void navigateHomePage(){
        BrowserActions.navigateTo(driver, PropertyReader.get("mainurl", String.class));
        JavascriptExecutor js = (JavascriptExecutor) driver;
// Remove elements by class or ID
        js.executeScript("document.querySelectorAll('.ad, .popup, .overlay, #ad-banner').forEach(el => el.remove());");
    }

    public void clickOnSubLink(String linkName){
        ElementActions.scrollToElement(driver,link(linkName));
        JSActions.clickUsingJavaScript(driver,link(linkName));
    }


}
