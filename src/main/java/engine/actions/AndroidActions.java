package engine.actions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class AndroidActions {

    public static void scrollToElement(AndroidDriver driver, By locator) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int startY = (int) (size.height * 0.80);
        int endY = (int) (size.height * 0.25);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        final int maxSwipes = 10;
        for (int i = 0; i < maxSwipes; i++) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.getFirst().isDisplayed()) {
                return;
            }
            Sequence swipe = new Sequence(finger, 1);
            swipe.addAction(finger.createPointerMove(
                    Duration.ZERO,
                    PointerInput.Origin.viewport(),
                    centerX,
                    startY));
            swipe.addAction(finger.createPointerDown(
                    PointerInput.MouseButton.LEFT.asArg()));

            swipe.addAction(finger.createPointerMove(
                    Duration.ofMillis(800),
                    PointerInput.Origin.viewport(),
                    centerX,
                    endY));

            swipe.addAction(finger.createPointerUp(
                    PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(List.of(swipe));
        }

        throw new NoSuchElementException(
                "Element not found after " + maxSwipes + " swipes: " + locator);
    }

    public static void scrollToText(AndroidDriver driver, String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().text(\"" + text + "\"));"));
    }

    public static void scrollToResourceId(AndroidDriver driver, String resourceId) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"));"));
    }

    public static void longClick(AndroidDriver driver, By locator,int time) {
        Waits.waitToBeVisible(locator);
        WebElement element = driver.findElement(locator);
        Rectangle rect = element.getRect();
        int x = rect.getX() + rect.getWidth() / 2;
        int y = rect.getY() + rect.getHeight() / 2;
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        sequence.addAction(
                finger.createPointerMove(Duration.ZERO,
                        PointerInput.Origin.viewport(), x, y));
        sequence.addAction(
                finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        sequence.addAction(
                new Pause(finger, Duration.ofSeconds(time)));

        sequence.addAction(
                finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(sequence));
    }

    public static void touchCoordinates(AndroidDriver driver, int xAxis, int yAxis) {
        PointerInput finger =
                new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                xAxis,
                yAxis));
        tap.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));

        tap.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(tap));
    }

    public static void pinchIn(AndroidDriver driver) {
        Map<String, Object> args = new HashMap<>();
        args.put("left", 100);
        args.put("top", 300);
        args.put("width", 800);
        args.put("height", 800);
        args.put("percent", 0.75);
        args.put("speed", 500);
        driver.executeScript("mobile: pinchCloseGesture", args);
    }

    public static void pinchOut(AndroidDriver driver) {
        Map<String, Object> args = new HashMap<>();
        args.put("left", 100);
        args.put("top", 300);
        args.put("width", 800);
        args.put("height", 800);
        args.put("percent", 0.75);
        args.put("speed", 500);
        driver.executeScript("mobile: pinchOpenGesture", args);
    }


    public static void dragAndDrop(
            AndroidDriver driver,
            WebElement source,
            WebElement target) {
        Point sourceLocation = source.getLocation();
        Dimension sourceSize = source.getSize();
        Point targetLocation = target.getLocation();
        Dimension targetSize = target.getSize();
        int sourceX = sourceLocation.getX() + sourceSize.getWidth() / 2;
        int sourceY = sourceLocation.getY() + sourceSize.getHeight() / 2;
        int targetX = targetLocation.getX() + targetSize.getWidth() / 2;
        int targetY = targetLocation.getY() + targetSize.getHeight() / 2;
        PointerInput finger =
                new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence drag = new Sequence(finger, 1);

        drag.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                sourceX,
                sourceY));

        drag.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));

        drag.addAction(finger.createPointerMove(
                Duration.ofMillis(800),
                PointerInput.Origin.viewport(),
                targetX,
                targetY));

        drag.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(drag));
    }

    public static void dragAndDrop(
            AndroidDriver driver,
            By source,
            By target,int time) {
        WebElement sourceElement= driver.findElement(source);
        WebElement targetElement= driver.findElement(target);
        Point sourceLocation = sourceElement.getLocation();
        Dimension sourceSize = sourceElement.getSize();
        Point targetLocation = targetElement.getLocation();
        Dimension targetSize = targetElement.getSize();
        int sourceX = sourceLocation.getX() + sourceSize.getWidth() / 2;
        int sourceY = sourceLocation.getY() + sourceSize.getHeight() / 2;
        int targetX = targetLocation.getX() + targetSize.getWidth() / 2;
        int targetY = targetLocation.getY() + targetSize.getHeight() / 2;
        PointerInput finger =
                new PointerInput(PointerInput.Kind.TOUCH, "finger");

        Sequence drag = new Sequence(finger, 1);

        drag.addAction(finger.createPointerMove(
                Duration.ZERO,
                PointerInput.Origin.viewport(),
                sourceX,
                sourceY));

        drag.addAction(finger.createPointerDown(
                PointerInput.MouseButton.LEFT.asArg()));
        drag.addAction(new Pause(finger, Duration.ofSeconds(time)));
        drag.addAction(finger.createPointerMove(
                Duration.ofMillis(800),
                PointerInput.Origin.viewport(),
                targetX,
                targetY));

        drag.addAction(finger.createPointerUp(
                PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(drag));
    }
}
