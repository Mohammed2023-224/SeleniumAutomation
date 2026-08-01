package engine.driver.androidDriver;

import io.appium.java_client.AppiumDriver;

public class AndroidDriverFactory {
    private AndroidDriverFactory(){}
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static AppiumDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(AppiumDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void unload() {
        driver.remove();
    }
}
