package engine.driver.androidDriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

public class AndroidDriverFactory {
    private AndroidDriverFactory(){}
    private static ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();

    public static AndroidDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(AppiumDriver driverInstance) {
        if (driverInstance instanceof AndroidDriver) {
            driver.set((AndroidDriver) driverInstance);
        } else {
            throw new IllegalArgumentException("Provided driver instance is not an AndroidDriver!");
        }
    }

    public static void unload() {
        driver.remove();
    }
}
