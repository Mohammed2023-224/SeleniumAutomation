package engine.driver.browsers;

import engine.driver.DriverOptions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Edge {

    // get driver options
    private EdgeOptions getDriverOptions() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments(DriverOptions.defineDriverOptions());
        return edgeOptions;
    }

    // initiate edge driver
    public WebDriver initiateDriver() {
        return new EdgeDriver(getDriverOptions());
    }
}
