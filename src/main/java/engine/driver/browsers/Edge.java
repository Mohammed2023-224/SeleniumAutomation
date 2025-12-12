package engine.driver.browsers;

import engine.constants.Constants;
import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Edge {

    // get driver options
    private EdgeOptions getDriverOptions() {
        DriverOptions driverOptions = new DriverOptions();
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments(driverOptions.defineDriverOptions());
        edgeOptions.setExperimentalOption("prefs",driverOptions.definePreferences());
        return edgeOptions;
    }

    // initiate edge driver
    public WebDriver initiateDriver() {
     Loggers.log.info("Start edge driver found at path: "+System.getProperty("webdriver.edge.driver"));
        return new EdgeDriver(getDriverOptions());
    }
    public WebDriver initiateRemoteDriver(String proxyURl) {
     Loggers.log.info("Start edge on remote driver port: "+proxyURl);
        try {
            return new RemoteWebDriver(new URL(proxyURl), getDriverOptions());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setLocalDriver(){
        if(Constants.localDriver.equalsIgnoreCase("true")){
            System.setProperty("webdriver.edge.driver", "src/main/resources/driver/msedgedriver.exe");
        }
    }
}
