package engine.actions.android;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class ElementSizeHelper {

    public static Point getElementCenter(AppiumDriver driver, By locator) {
        WebElement element = driver.findElement(locator);

        Point location = element.getLocation();
        Dimension size = element.getSize();

        return new Point(
                location.getX() + size.getWidth() / 2,
                location.getY() + size.getHeight() / 2
        );
    }

    public static Point getScreenCenter(AppiumDriver driver) {

        Dimension screenSize = driver.manage().window().getSize();

        return new Point(
                screenSize.getWidth() / 2,
                screenSize.getHeight() / 2
        );
    }

    public static Point getScreenSize(AppiumDriver driver) {

        Dimension screenSize = driver.manage().window().getSize();

        return new Point(
                screenSize.getWidth() ,
                screenSize.getHeight()
        );
    }
}
