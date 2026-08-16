package engine.actions.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class ElementSizeIdentifier {
    Point sourceLocation;
    Dimension sourceSize;

    public ElementSizeIdentifier(AppiumDriver driver,By locator) {
        WebElement ele = driver.findElement(locator);
        this.sourceLocation = ele.getLocation();
        this.sourceSize = ele.getSize();
    }

    public int getElementHeight(){
        return sourceSize.getHeight();
    }

    public int getElementWidth(){
        return sourceSize.getWidth();
    }

    public int getElementStartXLocation(){
        return sourceLocation.getX();
    }

    public int getElementStartYLocation(){
        return sourceLocation.getY();
    }

    public int getElementMiddleX(){
        return getElementStartXLocation()+getElementWidth()/2;
    }

    public int getElementMiddleY(){
        return getElementStartYLocation()+getElementHeight()/2;
    }
    
}
