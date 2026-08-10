package pages.appiumDemo;

import engine.actions.AndroidActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class DragAndDropScreen {
    private AndroidDriver driver;

    public DragAndDropScreen(AndroidDriver driver){
        this.driver=driver;
    }

    By redField=By.id("com.skill2lead.appiumdemo:id/layout1");
    By blueField=By.id("com.skill2lead.appiumdemo:id/layout2");
    By greenField=By.id("com.skill2lead.appiumdemo:id/layout3");
    By draggableBtn=By.id("com.skill2lead.appiumdemo:id/btnDrag");
    By draggableImg=By.id("com.skill2lead.appiumdemo:id/ingvw");
    By draggableText=By.id("com.skill2lead.appiumdemo:id/lbl");
    By pinchImg=By.id("com.skill2lead.appiumdemo:id/imageView");


    public void handleText(){
        Waits.waitToBeVisible(driver,draggableText);
        AndroidActions.dragAndDrop(driver,draggableText,greenField,1);
    }

    public void handleImg(){
        Waits.waitToBeVisible(driver,draggableImg);
        AndroidActions.dragAndDrop(driver,draggableImg,blueField,1);
    }

    public void handleBtn(){
        Waits.waitToBeVisible(driver,draggableBtn);
        AndroidActions.dragAndDrop(driver,draggableBtn,redField,1);
    }


    public void handlePinching(){
        Waits.waitToBeVisible(driver,pinchImg);
        AndroidActions.pinchIn(driver);
        AndroidActions.pinchOut(driver);
    }


}
