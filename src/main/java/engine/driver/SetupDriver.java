package engine.driver;

import engine.driver.browsers.Chrome;
import engine.driver.browsers.Edge;
import engine.enums.Browsers;
import org.openqa.selenium.WebDriver;

public class SetupDriver {

//    public WebDriver driver;

    public  WebDriver startDriver(Browsers browser ,boolean local,String proxy) {
        return switch (browser) {
            case EDGE -> local?new Edge().initiateDriver(): new Edge().initiateRemoteDriver(proxy);
            case CHROME -> local? new Chrome().initiateDriver(): new Chrome().initiateRemoteDriver(proxy);
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
