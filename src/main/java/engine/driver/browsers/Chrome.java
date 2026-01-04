package engine.driver.browsers;

import engine.constants.FrameworkConfigs;
import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
            if(FrameworkConfigs.chromeLocalDriverPath().isEmpty()){
            try {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                URL driverUrl = cl.getResource("driver/chromedriver.exe");
                if (driverUrl == null) {
                    throw new RuntimeException("chromedriver.exe not found in resources");
                }

                if (driverUrl.getProtocol().equals("jar")) {
                    try (InputStream in = driverUrl.openStream()) {
                        Path tempFile = Files.createTempFile("chromedriver", ".exe");
                        Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                        tempFile.toFile().setExecutable(true);
                        tempFile.toFile().deleteOnExit(); // Clean up on exit

                        System.setProperty("webdriver.chrome.driver", tempFile.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Path driverPath = Paths.get(driverUrl.toURI());
                    File driverFile = driverPath.toFile();
                    driverFile.setExecutable(true);
                    System.setProperty("webdriver.chrome.driver", driverFile.getAbsolutePath());
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }}
            else {
                System.setProperty("webdriver.chrome.driver", FrameworkConfigs.chromeLocalDriverPath());
            }
            Loggers.getLogger().info("chrome driver is found at path: " + System.getProperty("webdriver.chrome.driver"));

        }
    }
}
