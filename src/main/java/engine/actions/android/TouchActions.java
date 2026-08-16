package engine.actions.android;

import engine.exceptions.CustomExceptions;
import engine.reporters.Loggers;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;

import java.time.Duration;

public class TouchActions {

    public  static void longClick(AppiumDriver driver, int timeInMS, By locator){
        W3CTouchActions finger = new W3CTouchActions(driver,"finger");
        Point elementLocation=ElementSizeHelper.getElementCenter(driver,locator);
        finger.moveTo(0,elementLocation.getX(),elementLocation.getY());
        finger.addPointerDownAction();
        finger.addPauseAction(timeInMS);
        W3CActionsPerformer.perform(driver,finger);
        Loggers.logInfo("Clicking element located at "+ locator+" for "+timeInMS+" MS");
    }

    public  static void touchCoordinate(AppiumDriver driver, int xAxis ,int yAxis){
        W3CTouchActions finger = new W3CTouchActions(driver,"finger");
        Point screenSize=ElementSizeHelper.getScreenSize(driver);
        if(xAxis> screenSize.getX() || yAxis> screenSize.getY()){
            Loggers.logError("Wrong coordinates "+xAxis+" x "+yAxis+". Screen size is "+screenSize.getX()+" x "+screenSize.getY());
            throw  new CustomExceptions("Wrong coordinates");
        }
        finger.moveTo(0,xAxis,yAxis);
        finger.addPointerDownAction();
        W3CActionsPerformer.perform(driver,finger);
        Loggers.logInfo("clicking coordinates located at "+ xAxis+" x "+ yAxis);
    }

    public  static void pinchInOrOutHorizontally(AppiumDriver driver, int startDistance ,int endDistance, int timeInMS){
        Point center = ElementSizeHelper.getScreenCenter(driver);
        W3CTouchActions w3CTouchActions1=new W3CTouchActions(driver,"finger1");
        W3CTouchActions w3CTouchActions2=new W3CTouchActions(driver,"finger2");
        if(center.getX()-startDistance <0  ||center.getX() + endDistance> center.getX()*2
        ||center.getX()+startDistance >center.getX()*2  ||center.getX() - endDistance< 0
        ){
            Loggers.logError("x coordinates bypass the screenSize which is "+center.getX());
        }

        w3CTouchActions1.moveTo(0,center.getX()-startDistance, center.getY());
        w3CTouchActions1.addPointerDownAction();
        w3CTouchActions1.moveTo(
               timeInMS,
                center.getX() - endDistance,
                center.getY()
        );
        w3CTouchActions1.addPointerUpAction();
        w3CTouchActions2.moveTo(
                0,
                center.getX() + startDistance,
                center.getY()
        );
        w3CTouchActions2.addPointerDownAction();
        w3CTouchActions2.moveTo(
                timeInMS,
                center.getX() + endDistance,
                center.getY()
        );
        w3CTouchActions2.addPointerUpAction();
        W3CActionsPerformer.perform(
                driver,
                w3CTouchActions1,
                w3CTouchActions2
        );
        Loggers.logInfo("Pinching horizontally performed successfully");
    }

    public  static void dragAndDrop(AppiumDriver driver, By sourceElement ,By targetElement) {
        Point sourceCenter = ElementSizeHelper.getElementCenter(driver,sourceElement);
        Point targetCenter = ElementSizeHelper.getElementCenter(driver,targetElement);
        W3CTouchActions w3CTouchActions1=new W3CTouchActions(driver,"finger1");
        w3CTouchActions1.moveTo(0,sourceCenter.getX(), sourceCenter.getY());
        w3CTouchActions1.addPointerDownAction();
        w3CTouchActions1.addPauseAction(1000);
        w3CTouchActions1.moveTo(400,targetCenter.getX(), targetCenter.getY());
        w3CTouchActions1.addPointerUpAction();
        W3CActionsPerformer.perform(driver,w3CTouchActions1);
    }

    public  static void swipeDown(AppiumDriver driver) {
        Point screenSize = ElementSizeHelper.getScreenSize(driver);
        int centerX=screenSize.getX()/2;
        int startY=(int) (screenSize.getY()* 0.80);
        int endY=(int) (screenSize.getY()*0.25);
        W3CTouchActions w3CTouchActions1=new W3CTouchActions(driver,"finger1");
        w3CTouchActions1.moveTo(0,centerX,startY);
        w3CTouchActions1.addPointerDownAction();
        w3CTouchActions1.moveTo(400,centerX,endY);
        w3CTouchActions1.addPointerUpAction();
        W3CActionsPerformer.perform(driver,w3CTouchActions1);
    }
}
