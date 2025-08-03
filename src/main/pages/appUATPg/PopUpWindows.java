package pages.appUATPg;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class PopUpWindows {
    private final WebDriver driver;
    public PopUpWindows(WebDriver driver){
        this.driver=driver;
    }
    By firstWindow= By.id("window1");
    By secondWindow= By.id("window2");
    By firstWindowAssertionButton= By.id("click_me_2");
    By secondWindowAssertionButton= By.id("click_me_4");

    public void handlePopups(){
        ElementActions.clickElement(driver,firstWindow);
        BrowserActions.switchToWindowByIndex(driver,1);
        ElementActions.clickElement(driver,firstWindowAssertionButton);
        Assert.assertTrue(ElementActions.getText(driver,firstWindowAssertionButton).contains("Clicked"));
        driver.close();
        BrowserActions.switchToWindowByIndex(driver,0);
        ElementActions.clickElement(driver,secondWindow);
        BrowserActions.switchToWindowByIndex(driver,1);
        ElementActions.clickElement(driver,secondWindowAssertionButton);
        Assert.assertTrue(ElementActions.getText(driver,secondWindowAssertionButton).contains("Clicked"));
        driver.close();
        BrowserActions.switchToWindowByIndex(driver,0);
    }
}
