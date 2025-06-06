package engine.driver.browsers;

import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Chrome {

    // get driver options
    private ChromeOptions getDriverOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(DriverOptions.defineDriverOptions());
        return chromeOptions;
    }

    // initiate chrome driver
    public WebDriver initiateDriver() {
        Loggers.log.info("Start chrome driver");
        return new ChromeDriver(getDriverOptions());
    }

}
