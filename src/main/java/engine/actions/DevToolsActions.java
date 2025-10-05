package engine.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v137.network.Network;
import org.openqa.selenium.devtools.v137.network.model.Headers;
import org.openqa.selenium.devtools.v137.page.Page;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DevToolsActions {
    private WebDriver driver;
    DevTools devTools;

    public DevToolsActions(WebDriver driver){
        this.driver=driver;
         devTools = ((ChromeDriver) driver).getDevTools();
    }

    public DevToolsActions  createSession(){
        devTools.createSession();
        return this;
    }

    public void setDownloadFolder(String filePath){
        devTools.send(Page.setDownloadBehavior(
                Page.SetDownloadBehaviorBehavior.ALLOW,  //
                Optional.of(filePath)));
    }

    public void handleBasicAuth() {
        Map<String, Object> headers = new HashMap<>();
        String basicAuth = Base64.getEncoder().encodeToString("admin:admin".getBytes());
        headers.put("Authorization", "Basic " + basicAuth);
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
    }
}
