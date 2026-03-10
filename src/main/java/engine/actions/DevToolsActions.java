package engine.actions;

import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v141.network.Network;
import org.openqa.selenium.devtools.v141.network.model.Headers;
import org.openqa.selenium.devtools.v141.page.Page;
import org.openqa.selenium.edge.EdgeDriver;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DevToolsActions {
    private DevTools devTools;

    public DevToolsActions(WebDriver driver){
        if(driver instanceof ChromeDriver ) {
            devTools = ((ChromeDriver) driver).getDevTools();
        } else if (driver instanceof EdgeDriver) {
            devTools = ((EdgeDriver) driver).getDevTools();
        }
        else {
            Loggers.getLogger().error("Couldn't initiate devtools actions");
        }
    }

    public DevToolsActions  createSession(){
        devTools.createSession();
        Loggers.getLogger().info("Create devtools session");
        return this;
    }

    public void setDownloadFolder(String filePath){
        devTools.send(Page.setDownloadBehavior(
                Page.SetDownloadBehaviorBehavior.ALLOW,  //
                Optional.of(filePath)));
        Loggers.getLogger().info("set file Download Path at: {}", filePath);

    }

    public void handleBasicAuth(String username,String password) {
        Map<String, Object> headers = new HashMap<>();
        String credentials = String.format("%s:%s", username, password);
        String basicAuth = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.put("Authorization", "Basic " + basicAuth);
        devTools.send(Network.enable(Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setExtraHTTPHeaders(new Headers(headers)));
        Loggers.getLogger().info("add basic auth headers through dev tools as user name: {} password: {}",username, password);

    }
}
