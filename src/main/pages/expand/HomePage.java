package pages.expand;

import engine.actions.BrowserActions;
import engine.constants.Constants;
import org.openqa.selenium.WebDriver;

public class HomePage {
        public WebDriver driver;
        public HomePage(WebDriver driver){
            this.driver=driver;
        }

    public void navigateHomePage(){
        BrowserActions.navigateTo(driver, Constants.url);
    }
}
