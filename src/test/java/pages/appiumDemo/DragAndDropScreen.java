package pages.appiumDemo;

import engine.actions.android.*;
import engine.actions.Waits;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;

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
        TouchActions.dragAndDrop(driver,draggableText,greenField);
    }

    public void handleImg(){
        Waits.waitToBeVisible(driver,draggableImg);
        TouchActions.dragAndDrop(driver,draggableImg,blueField);
    }

    public void handleBtn(){
        Waits.waitToBeVisible(driver,draggableBtn);
        TouchActions.dragAndDrop(driver,draggableBtn,redField);
    }


    public void handlePinching(){
        Waits.waitToBeVisible(driver,pinchImg);
        TouchActions.pinchInOrOutHorizontally(driver,400,50,800);
        TouchActions.pinchInOrOutHorizontally(driver,50,400,800);
    }


}
