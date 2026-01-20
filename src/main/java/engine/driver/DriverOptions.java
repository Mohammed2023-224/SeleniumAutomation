package engine.driver;

import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.Paths;
import java.util.*;

public class DriverOptions {
private LinkedHashSet<String> userDriverOptions=new LinkedHashSet<>();
private Map<String,Object> userDriverPreferences=new LinkedHashMap<>();
private Map<String,Object> userDrivercapabilities=new LinkedHashMap<>();

    public Set<String> defineDriverOptions(){
        Set<String> opts = new LinkedHashSet<>();
        if(FrameworkConfigs.headless()){
            opts.add("--headless=new");
            opts.add("--window-size=1920,1080");
         Loggers.getLogger().info("Activate Headless mode");
        }
        if(FrameworkConfigs.maximized()){
            opts.add("start-maximized");
         Loggers.getLogger().info("Start maximized");
        }
        if(FrameworkConfigs.popupBlocker()){
            opts.add("--disable-popup-blocking");
         Loggers.getLogger().info("disable popup blocking");
        }
        Collections.addAll(opts,
                "--disable-save-password-bubble",
                "--disable-notifications"
        );
        if(!userDriverOptions.isEmpty()) opts.addAll(userDriverOptions);
        return opts;
    }

    public void setUserDriverOptions(Set<String> list){
        userDriverOptions= new LinkedHashSet<>(list);
    }
    public void setUserDriverPreferences(Map<String,Object> pref){
        userDriverPreferences= new LinkedHashMap<>(pref);
    }

    public void setUserDesiredCapabilities(Map<String,Object> pref){
        userDrivercapabilities= new LinkedHashMap<>(pref);
    }

    public  Map<String,Object> definePreferences(){
        Map<String, Object> prefs = new LinkedHashMap<>();
        prefs.put("download.default_directory", Paths.get(
                System.getProperty("user.dir"),
                FrameworkConfigs.downloadsPath()
        ).toAbsolutePath().toString());
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.manager.showWhenStarting", false);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        if(!userDriverPreferences.isEmpty()) prefs.putAll(userDriverPreferences);
        return  prefs;
    }

    public DesiredCapabilities defineDesiredCapabilities(){
        DesiredCapabilities desiredCapabilities=new DesiredCapabilities();
        Map<String, Object> bstackOptions = new LinkedHashMap<>();
        bstackOptions.put("local", false);
        bstackOptions.put("projectName", "test");
        bstackOptions.put("buildName", "my build");
        bstackOptions.put("sessionName", "Test Session");
        if(!userDrivercapabilities.isEmpty()) bstackOptions.putAll(userDrivercapabilities);
        desiredCapabilities.setCapability("bstack:options", bstackOptions);
        return desiredCapabilities;
    }
}
