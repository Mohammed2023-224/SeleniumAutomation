package engine.driver;

import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.JSONReader;
import engine.utils.PropertyReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DriverOptions {
//TODO Needs huge maintainance


    public  ArrayList<String> defineDriverOptions(){
        ArrayList<String> opts=new ArrayList<>();
        if(Constants.headlessMode.equalsIgnoreCase("true")){
            opts.add("--headless=new");
            opts.add("--window-size=1920,1080");
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

//        Collections.addAll(opts,
//                "--disable-save-password-bubble",
//                "--disable-blink-features=AutomationControlled",
//                "--safebrowsing-disable-download-protection",
//                "--no-default-browser-check",
//                "--disable-notifications",
//                "--disable-extensions",
//                "--blink-settings=imagesEnabled=false",
//                "--disable-features=InterestCohortAPI,SameSiteByDefaultCookies,CookiesWithoutSameSiteMustBeSecure",
//                "--disable-features=PreloadMediaEngagementData,MediaRouter",
//                "--disable-features=DownloadBubble",
//                "--disable-features=DownloadsUX",
//                "--suppress-download-notification"
//        );
        return opts;
    }

    public  Map<String,Object> definePreferences(){
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", Constants.downloadsPath); // Make sure this path exists!
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);
        prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
        prefs.put("safebrowsing.enabled", true); // sometimes affects prompts
//        prefs.put("plugins.always_open_pdf_externally", true);
        return  prefs;
    }

}
