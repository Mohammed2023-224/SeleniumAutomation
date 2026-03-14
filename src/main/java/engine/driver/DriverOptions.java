package engine.driver;

import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;
import org.openqa.selenium.WebDriver;

import java.nio.file.Paths;
import java.util.*;

public class DriverOptions {
private static LinkedHashSet<String> userDriverOptions=new LinkedHashSet<>();
private static Map<String,Object> userDriverPreferences=new LinkedHashMap<>();

    public Set<String> defineDriverOptions(){
        Set<String> opts = new LinkedHashSet<>();
        if(FrameworkConfigs.headless()){
            opts.add("--headless=new");
         Loggers.getLogger().info("Activate Headless mode");
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

    public static void setUserDriverOptions(Set<String> list){
        userDriverOptions= new LinkedHashSet<>(list);
    }
    public static void setUserDriverPreferences(Map<String,Object> pref){
        userDriverPreferences= new LinkedHashMap<>(pref);
    }


    public  Map<String,Object> definePreferences() {
        Map<String, Object> prefs = new LinkedHashMap<>();
        prefs.put("download.default_directory", Paths.get(
                System.getProperty("user.dir"),
                FrameworkConfigs.downloadsPath()
        ).toAbsolutePath().toString());
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.password_manager_leak_detection",false);
        prefs.put("download.manager.showWhenStarting", false);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        if (!userDriverPreferences.isEmpty()) prefs.putAll(userDriverPreferences);
        return prefs;
    }

    public static void maximizeWindow(WebDriver driver,Boolean value){
        if(Boolean.TRUE.equals(value)){
            driver.manage().window().maximize();
            Loggers.getLogger().info("maximize window");
        }
    }
}
