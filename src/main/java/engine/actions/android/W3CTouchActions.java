package engine.actions.android;

import engine.reporters.Loggers;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;

public class W3CTouchActions {
    PointerInput finger;
    @Getter
    Sequence sequence;
    AppiumDriver driver;
    String fingerName="";

    public W3CTouchActions(AppiumDriver driver,String fingerName){
        this.finger=new PointerInput(PointerInput.Kind.TOUCH,fingerName);
        this.sequence=new Sequence(this.finger,0);
        this.driver=driver;
        this.fingerName=fingerName;
    }

    public void addPointerDownAction(){
        this.sequence.addAction(this.finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        Loggers.logInfo("Add tab down action to sequence in "+fingerName);
    }

    public void addPointerUpAction(){
        this.sequence.addAction(this.finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        Loggers.logInfo("Add tab up action to sequence in "+fingerName);
    }

    public void moveTo(int timeInMS, int xAxis,int yAxis ){
        this.sequence.addAction(this.finger.createPointerMove(Duration.ofMillis(timeInMS),PointerInput.Origin.viewport(),xAxis,yAxis));
        Loggers.logInfo("Add move finger action from top left of the screen to x axis: "+xAxis + " and Y axis: "+yAxis+" to sequence in "+fingerName);
    }

    public void moveTo(int timeInMS,By locator ){
        Point center = ElementSizeHelper.getElementCenter(driver,locator);
        int elementMiddleWidth= center.getX();
        int elementMiddleHeight= center.getY();
        this.sequence.addAction(this.finger.createPointerMove(Duration.ofMillis(timeInMS),PointerInput.Origin.viewport(),elementMiddleWidth
                , elementMiddleHeight));
        Loggers.logInfo("Add move finger action from top left of the screen to element located at x axis: "+elementMiddleWidth + " " +
                "and y axis: "+elementMiddleHeight +" to sequence in "+fingerName);
    }

    public void moveTo(int timeInMS, By element, int xAxis, int yAxis ){
        this.sequence.addAction(this.finger.createPointerMove(Duration.ofMillis(timeInMS),PointerInput.Origin.fromElement(driver.findElement(element)),xAxis,yAxis));
        Loggers.logInfo("Add move finger action from element: "+element+" top left corner to x axis: "+xAxis +
                " and y axis: "+yAxis+" to sequence in "+fingerName);
    }

    public void addPauseAction(int timeInMS){
        this.sequence.addAction(new Pause(this.finger, Duration.ofMillis(timeInMS)));
        Loggers.logInfo("Add pause action by "+timeInMS+" MS to sequence in "+fingerName);
    }

}
