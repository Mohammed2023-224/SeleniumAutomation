package engine.driver;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Capabilities {

    public Map<String,Object> build(String browser){
    Map<String,Object> m=new HashMap<>();
        // REQUIRED
        m.put("browserName", browser);
        m.put("browserVersion", "latest");
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "11");
        bstackOptions.put("projectName", "SeleniumAutomation");
        bstackOptions.put("buildName", "Local Debug Build "+ LocalDateTime.now());
        bstackOptions.put("sessionName", browser+" Test");

        m.put("bstack:options", bstackOptions);
        return m;

    }
}
