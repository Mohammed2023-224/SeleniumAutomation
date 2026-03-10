package engine.driver.browsers;

import org.openqa.selenium.WebDriver;

import java.util.Map;

public interface BrowserDriver {
    WebDriver initiateDriver();
    WebDriver initiateRemoteDriver(String remoteUrl, Map<String,Object> caps);
}
