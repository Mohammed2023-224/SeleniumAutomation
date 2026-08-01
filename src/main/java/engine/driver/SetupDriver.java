package engine.driver;

import engine.constants.FrameworkConfigs;
import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import engine.driver.browsers.FireFox;
import engine.enums.Browsers;
import engine.exceptions.CustomExceptions;
import engine.reporters.Loggers;
import engine.utils.propertyFilesHandlers.PropertyLoader;
import engine.utils.propertyFilesHandlers.PropertyReader;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static engine.driver.DriverHelper.waitForRemoteUrl;

public class SetupDriver {

    public  WebDriver startDriver(String browser ,boolean local,String osVersion,String browserVersion,String os) {
        try {
            Browsers enumBrowser = Browsers.valueOf((browser == null || browser.isEmpty() ? FrameworkConfigs.browser() : browser).toUpperCase());
            String port = System.getProperty("port");
            boolean useCapabilityClass =
                    Boolean.TRUE.equals(PropertyReader.get("use_capability_class", Boolean.class));
            Map<String, Object> caps = useCapabilityClass && !local ?
                    new Capabilities().browserStackCapabilities(enumBrowser.toString(), osVersion, browserVersion, os) : new HashMap<>();
            String resolvedPort = (port == null || port.isEmpty()) ? FrameworkConfigs.proxy() : port;
            port = local ? "" : resolvedPort;
            if (!local) waitForRemoteUrl(port, 10);
            if (!local && (port == null || port.isEmpty()))
                throw new IllegalStateException("Port or grid URL must be specified for remote execution");
            return switch (enumBrowser) {
                case EDGE -> local ? new Edge().initiateDriver() : new Edge().initiateRemoteDriver(port, caps);
                case CHROME -> local ? new Chrome().initiateDriver() : new Chrome().initiateRemoteDriver(port, caps);
                case FIREFOX -> local ? new FireFox().initiateDriver() : new FireFox().initiateRemoteDriver(port, caps);
                default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
            };
        } catch (Exception e) {
            Loggers.logError("Couldn't initiate the driver");
            throw e;
        }
    }

    public AppiumDriver startMobileDriver(){
        DesiredCapabilities desiredCapabilities=new DesiredCapabilities();
        (PropertyLoader.loadAsMap(PropertyReader.get("desiredCapabilityPath", String.class))).forEach(
                desiredCapabilities::setCapability);
        String urlString=PropertyReader.get("appiumURL",String.class);
        Loggers.logInfo("Add the following capacilities: "+ desiredCapabilities);
        Loggers.logInfo("add the following url: "+ urlString);
        URL url;
        try {
             url =new URL(urlString);
        } catch (MalformedURLException e) {
            throw new CustomExceptions("Couldn't navigate to the url provided : "+ urlString + e.getMessage());
        }
        return new AppiumDriver(url,desiredCapabilities);
    }
}
