package engine.driver;

import engine.constants.FrameworkConfigs;
import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import engine.enums.Browsers;
import org.openqa.selenium.WebDriver;

import static engine.driver.DriverHelper.waitForRemoteUrl;

public class SetupDriver {

//    public WebDriver driver;

    public  WebDriver startDriver(String browser ,boolean local) {
        Browsers enumBrowser =Browsers.valueOf((browser == null || browser.isEmpty() ? FrameworkConfigs.browser() : browser).toUpperCase());
        String port=System.getProperty("port");
        port = local?"":port == null || port.isEmpty()? FrameworkConfigs.proxy():port;
        if(FrameworkConfigs.gridEnabled()) waitForRemoteUrl(port,10);
        if(!local && (port==null||port.isEmpty()))  throw new IllegalStateException("Port or grid URL must be specified for remote execution");
        return switch (enumBrowser) {
            case EDGE -> local?new Edge().initiateDriver(): new Edge().initiateRemoteDriver(port);
            case CHROME -> local? new Chrome().initiateDriver(): new Chrome().initiateRemoteDriver(port);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    public WebDriver startDriverRemotely(String browser,String proxy) {
        return switch (browser.toLowerCase()) {
            case "edge" -> new Edge().initiateRemoteDriver(proxy);
            case "chrome" -> new Chrome().initiateRemoteDriver(proxy);
            default -> throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        };
    }
}
