package pages.appUATPg;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Frames {

    private final WebDriver driver;
    By firstFrame=By.id("frame1");
    By secondFrame=By.id("frame2");
    By thirdFrame=By.id("frame3");
    By fourthFrame=By.id("frame4");
    By firstEle=By.id("click_me_2");
    By secondEle=By.id("click_me_4");
    public Frames(WebDriver driver){
        this.driver=driver;
    }

    public void handleFrames(){

        ElementActions.switchToFrameByLocator(driver,firstFrame);
        ElementActions.switchToFrameByLocator(driver,secondFrame);
        ElementActions.clickElement(driver,firstEle);
        Assert.assertTrue(ElementActions.getText(driver,firstEle).contains("Clicked"));
        ElementActions.switchToParentFrame(driver);
        ElementActions.switchToFrameByLocator(driver,thirdFrame);
        ElementActions.switchToFrameByLocator(driver,fourthFrame);
        ElementActions.clickElement(driver,secondEle);
        Assert.assertTrue(ElementActions.getText(driver,secondEle).contains("Clicked"));


    }
}
