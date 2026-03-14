package engine.driver.browsers;

import engine.constants.FrameworkConfigs;
import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import engine.utils.ClassPathLoading;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class FireFox implements  BrowserDriver{
// Can always download using webdriver manager as it gives more control then manual downloads
    // get driver options
    private FirefoxOptions getDriverOptions() {
        DriverOptions driverOptions = new DriverOptions();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments(new ArrayList<>(driverOptions.defineDriverOptions()));

        return firefoxOptions;
    }

    // initiate chrome driver
    public WebDriver initiateDriver() {
        setLocalDriver();
        Loggers.getLogger().info("Start chrome driver " );
        return new FirefoxDriver(getDriverOptions());
    }


    public WebDriver initiateRemoteDriver(String proxyURl, Map<String,Object> caps) {
     Loggers.getLogger().info("Start firefox on remote driver port: {}",proxyURl);
        try {
            FirefoxOptions options=getDriverOptions();
            if(!caps.isEmpty()) {caps.forEach(options::setCapability);}
            RemoteWebDriver remoteWebDriver= new RemoteWebDriver(new URI(proxyURl).toURL(), options);
            remoteWebDriver.setFileDetector(new LocalFileDetector());
            return remoteWebDriver;
        } catch (MalformedURLException e) {
            Loggers.getLogger().error("Malformed URl ",e);
            return null;
        } catch (URISyntaxException e) {
            Loggers.getLogger().error("syntax error URl ",e);
            return null;
        }

    }

    private void setLocalDriver() {
        String webDriverPropertyPath="webdriver.chrome.driver";
        if (FrameworkConfigs.localPathDriver()) {
            if(FrameworkConfigs.chromeLocalDriverPath().isEmpty()) {
                Path path = ClassPathLoading.getResourceAsPath("driver/chromedriver.exe", true);
                System.setProperty(webDriverPropertyPath, path.toString());
            }
            else {
                System.setProperty(webDriverPropertyPath, FrameworkConfigs.chromeLocalDriverPath());
            }
            Loggers.getLogger().info("chrome driver is found at path: {}" ,
                    System.getProperty(webDriverPropertyPath).isEmpty()?"test log":System.getProperty(webDriverPropertyPath));

        }
    }
}
