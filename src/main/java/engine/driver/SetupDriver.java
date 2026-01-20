package engine.driver;

import engine.constants.FrameworkConfigs;
import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import engine.enums.Browsers;
import engine.reporters.Loggers;
import engine.utils.PropertyReader;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

import static engine.driver.DriverHelper.waitForRemoteUrl;

public class SetupDriver {

    public  WebDriver startDriver(String browser ,boolean local) {
        Browsers enumBrowser =Browsers.valueOf((browser == null || browser.isEmpty() ? FrameworkConfigs.browser() : browser).toUpperCase());
        String port=System.getProperty("port");
        Map<String,Object> caps= PropertyReader.get("use_capability_class", Boolean.class) ||!local?
                new Capabilities().build(browser): new HashMap<>();
        port = local?"":port == null || port.isEmpty()? FrameworkConfigs.proxy():port;
        if(!local) waitForRemoteUrl(port,10);
        if(!local && (port==null||port.isEmpty()))  throw new IllegalStateException("Port or grid URL must be specified for remote execution");
        return switch (enumBrowser) {
            case EDGE -> local?new Edge().initiateDriver(): new Edge().initiateRemoteDriver(port,caps);
            case CHROME -> local? new Chrome().initiateDriver(): new Chrome().initiateRemoteDriver(port,caps);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }
}
