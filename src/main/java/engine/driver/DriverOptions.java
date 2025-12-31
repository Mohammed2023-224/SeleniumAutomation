package engine.driver;

import engine.constants.FrameworkConfigs;
import engine.reporters.Loggers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DriverOptions {

    public  ArrayList<String> defineDriverOptions(){
        ArrayList<String> opts=new ArrayList<>();
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
        return opts;
    }

    public  Map<String,Object> definePreferences(){
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", FrameworkConfigs.downloadsPath());
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
        prefs.put("safebrowsing.enabled", true); // sometimes affects prompts
//        prefs.put("plugins.always_open_pdf_externally", true);
        return  prefs;
    }

}
