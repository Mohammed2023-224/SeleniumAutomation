package engine.driver.browsers;

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
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments(new DriverOptions().defineDriverOptions());
        edgeOptions.setExperimentalOption("prefs",new DriverOptions().definePreferences());
        return edgeOptions;
    }

    // initiate edge driver
    public WebDriver initiateDriver() {
        Loggers.getInstance().log.info("Start edge driver");
        return new EdgeDriver(getDriverOptions());
    }
    public WebDriver initiateRemoteDriver(String proxyURl) {
        Loggers.getInstance().log.info("Start edge on remote driver");
        try {
            return new RemoteWebDriver(new URL(proxyURl), getDriverOptions());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
