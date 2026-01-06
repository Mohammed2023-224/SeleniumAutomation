package engine.driver.browsers;

import engine.constants.FrameworkConfigs;
import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import engine.utils.ClassPathLoading;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public class Chrome implements  BrowserDriver{
// Can always download using webdriver manager as it gives more control then manual downloads
    // get driver options
    private ChromeOptions getDriverOptions() {
        DriverOptions driverOptions = new DriverOptions();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments( driverOptions.defineDriverOptions());
        chromeOptions.setExperimentalOption("prefs",driverOptions.definePreferences());

        return chromeOptions;
    }

    // initiate chrome driver
    public WebDriver initiateDriver() {
        setLocalDriver();
        Loggers.getLogger().info("Start chrome driver " );
        return new ChromeDriver(getDriverOptions());
    }


    public WebDriver initiateRemoteDriver(String proxyURl) {
     Loggers.getLogger().info("Start chrome on remote driver port: "+proxyURl);
        try {
            return new RemoteWebDriver(new URL(proxyURl), getDriverOptions());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setLocalDriver() {
        if (FrameworkConfigs.localPathDriver()) {
            if(FrameworkConfigs.chromeLocalDriverPath().isEmpty()) {
                Path path = ClassPathLoading.getResourceAsPath("driver/chromedriver.exe", true);
                System.setProperty("webdriver.chrome.driver", path.toString());
            }
            else {
                System.setProperty("webdriver.chrome.driver", FrameworkConfigs.chromeLocalDriverPath());
            }
            Loggers.getLogger().info("chrome driver is found at path: " + System.getProperty("webdriver.chrome.driver"));

        }
    }
}
