package engine.driver;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Capabilities {

    //Can enhance more to be more dynamic for only one session
    public Map<String,Object> browserStackCapabilities(String browser, String osVersion, String browserVersion
    , String os){
    Map<String,Object> m=new HashMap<>();
        // REQUIRED
        m.put("browserName", browser);
        m.put("browserVersion", browserVersion.isEmpty()?"latest":browserVersion);
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", os.isEmpty()?"Windows":os);
        bstackOptions.put("osVersion", osVersion.isEmpty()?"11":osVersion);
        bstackOptions.put("projectName", "SeleniumAutomation");
        bstackOptions.put("buildName", "test build "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        bstackOptions.put("sessionName", browser+" Test");

        m.put("bstack:options", bstackOptions);
        return m;

    }
}
