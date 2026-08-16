package engine.actions.android;

import engine.actions.Waits;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class AndroidActions {

    public static void scrollToElement(AndroidDriver driver, By locator) {
        final int maxSwipes = 10;
        for (int i = 0; i < maxSwipes; i++) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.getFirst().isDisplayed()) {
                return;
            }
            TouchActions.swipeDown(driver);
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

}
