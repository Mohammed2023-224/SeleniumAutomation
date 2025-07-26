package engine.driver;

import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.JSONReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverOptions {

     ArrayList<String> opts=new ArrayList<>();
    Map<String, Object> prefs = new HashMap<>();

    public  ArrayList<String> defineDriverOptions(){
        if(Constants.headlessMode.equalsIgnoreCase("true")){
            opts.add("--headless");
            Loggers.log.info("Activate Headless mode");
        }
        if(Constants.maximizedMode.equalsIgnoreCase("true")){
            opts.add("start-maximized");
            Loggers.log.info("Start maximized");
        }
        if(Constants.popupBlocker.equalsIgnoreCase("true")){
            opts.add("--disable-popup-blocking");
            Loggers.log.info("disable popup blocking");
        }
        opts.add("--disable-save-password-bubble");
        opts.add("--disable-infobars");
        opts.add("disable-autofill");
        opts.add("--incognito");
        opts.add("--disable-blink-features=AutomationControlled");
//        opts.add()
        return opts;
    }

    public  Map<String,Object> definePreferences(){
//        prefs.put("download.default_directory", "C:\\Users\\USER\\Downloads"); // Make sure this path exists!
//        prefs.put("profile.default_content_settings.popups", 0);
//        prefs.put("download.prompt_for_download", false);
//        prefs.put("plugins.always_open_pdf_externally", true);
        return  prefs;
    }

}
