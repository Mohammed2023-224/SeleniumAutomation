package engine.actions;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class AndroidActions {

    public static void scrollToElement(AppiumDriver driver, By locator) {
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

    public static void scrollToText(AppiumDriver driver, String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().text(\"" + text + "\"));"));
    }

    public static void scrollToResourceId(AppiumDriver driver, String resourceId) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"));"));
    }

    public static void longClick(AppiumDriver driver, By locator,int time) {
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
}
