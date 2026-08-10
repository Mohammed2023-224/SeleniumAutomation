package pages.appiumDemo;

import engine.actions.AndroidActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomeScreen {
    private AndroidDriver driver;

    public HomeScreen(AndroidDriver driver){
        this.driver=driver;
    }

    private By appTitle=By.xpath("//android.widget.TextView[@text=\"Appium Demo\"]");

    private By getLinkButton(HomePageLinks homePageLinks){
        return By.id("com.skill2lead.appiumdemo:id/"+homePageLinks.getMethod());
    }

    public void clickOnLink(HomePageLinks homePageLinks){
        Waits.waitToBeVisible(appTitle);
        AndroidActions.scrollToElement(driver,getLinkButton(homePageLinks));
        ElementActions.clickElement(driver,getLinkButton(homePageLinks));
    }

    public void longClickLink(HomePageLinks homePageLinks){
        Waits.waitToBeVisible(appTitle);
        AndroidActions.longClick(driver,getLinkButton(homePageLinks),2);
    }

    public void waitLinkVisibility(HomePageLinks homePageLinks){
        Waits.waitToBeVisible(getLinkButton(homePageLinks));
    }



}
