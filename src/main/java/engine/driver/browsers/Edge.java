package engine.driver.browsers;

import engine.constants.FrameworkConfigs;
import engine.driver.DriverOptions;
import engine.reporters.Loggers;
import engine.utils.ClassPathLoading;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;


public class Edge  implements  BrowserDriver{

    // get driver options
    private EdgeOptions getDriverOptions() {
        DriverOptions driverOptions = new DriverOptions();
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments( new ArrayList<>(driverOptions.defineDriverOptions()));
        edgeOptions.setExperimentalOption("prefs",driverOptions.definePreferences());
        return edgeOptions;
    }

    // initiate edge driver
    public WebDriver initiateDriver() {
        setLocalDriver();
     Loggers.getLogger().info("Start edge driver ");
        return new EdgeDriver(getDriverOptions());
    }
    public WebDriver initiateRemoteDriver(String proxyURl, Map<String,Object> caps) {
     Loggers.getLogger().info("Start edge on remote driver port: {}",proxyURl);
        try {
            EdgeOptions options=getDriverOptions();
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
    private void setLocalDriver(){
        if(FrameworkConfigs.localPathDriver()){
            String localEdgePath="webdriver.edge.driver";
            if(FrameworkConfigs.edgeLocalDriverPath().isEmpty()) {
                Path path = ClassPathLoading.getResourceAsPath("driver/msedgedriver.exe", true);
                System.setProperty(localEdgePath, path.toString());
            }
            else {
                System.setProperty(localEdgePath, FrameworkConfigs.edgeLocalDriverPath());
            }
            Loggers.getLogger().info("Edge driver is found at path: {}",
                    System.getProperty(localEdgePath).isEmpty()?"test log":System.getProperty(localEdgePath));

        }
    }
}
