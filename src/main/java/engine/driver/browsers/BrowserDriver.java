package engine.driver.browsers;

import org.openqa.selenium.WebDriver;

public interface BrowserDriver {
    WebDriver initiateDriver();
    WebDriver initiateRemoteDriver(String remoteUrl);
}
