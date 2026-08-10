package pages.appiumDemo;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class TabViewScreen {
    private AndroidDriver driver;

    public TabViewScreen(AndroidDriver driver){
        this.driver=driver;
    }
    By screenTitle=By.xpath("//android.widget.TextView[@text=\"Tab View\"]");
    By text=By.xpath("//android.widget.TextView[@text=\"Home fragment\"]");

    private By buttonsLocator(String text){
        return By.xpath("//android.widget.TextView[@text=\""+text+"\"]");
    }

    private void waitTitleScreen(){
        Waits.waitToBeVisible(screenTitle);
    }

    //SPORT HOME MOVIE
    public void clickTab(String text){
        waitTitleScreen();
        ElementActions.clickElement(driver,buttonsLocator(text));
    }

    public void asserTextView(){
        HardAssertions.assertTru(()-> ElementActions.getText(driver,text).contains("Home"),"Assert correct view");
    }

}
