package engine.driver.browsers;

import engine.constants.FrameworkConfigs;
import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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

public class Edge  implements  BrowserDriver{

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
        setLocalDriver();
     Loggers.getLogger().info("Start edge driver ");
        return new EdgeDriver(getDriverOptions());
    }
    public WebDriver initiateRemoteDriver(String proxyURl) {
     Loggers.getLogger().info("Start edge on remote driver port: "+proxyURl);
        try {
            return new RemoteWebDriver(new URL(proxyURl), getDriverOptions());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
//TODO Move class loader to utility class
    private void setLocalDriver(){
        if(FrameworkConfigs.localPathDriver()){
            if(FrameworkConfigs.edgeLocalDriverPath().isEmpty()){
                try {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    URL driverUrl = cl.getResource("driver/msedgedriver.exe");
                    if (driverUrl == null) {
                        throw new RuntimeException("edgedriver.exe not found in resources");
                    }
                    if (driverUrl.getProtocol().equals("jar")) {
                        try (InputStream in = driverUrl.openStream()) {
                            Path tempFile = Files.createTempFile("edgedriver", ".exe");
                            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                            tempFile.toFile().setExecutable(true);
                            tempFile.toFile().deleteOnExit(); // Clean up on exit
                            System.setProperty("webdriver.edge.driver", tempFile.toString());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Path driverPath = Paths.get(driverUrl.toURI());
                        File driverFile = driverPath.toFile();
                        driverFile.setExecutable(true);
                        System.setProperty("webdriver.edge.driver", driverFile.getAbsolutePath());
                    }
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }}
            else {
                System.setProperty("webdriver.edge.driver", FrameworkConfigs.edgeLocalDriverPath());
            }
            Loggers.getLogger().info("Edge driver is found at path: "+System.getProperty("webdriver.edge.driver"));

        }
    }
}
