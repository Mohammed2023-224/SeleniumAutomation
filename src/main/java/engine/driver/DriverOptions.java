package engine.driver;

import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.JSONReader;
import engine.utils.PropertyReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverOptions {

     ArrayList<String> opts=new ArrayList<>();
    Map<String, Object> prefs = new HashMap<>();

    public  ArrayList<String> defineDriverOptions(){
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
        opts.add("--disable-save-password-bubble");
        opts.add("--disable-infobars");
        opts.add("disable-autofill");
//        opts.add("--incognito");
        opts.add("--disable-blink-features=AutomationControlled");
        opts.add("--disable-popup-blocking");
        opts.add("--safebrowsing-disable-download-protection");
        opts.add("--no-default-browser-check");
        opts.add("--no-first-run");
        opts.add("--disable-notifications");
        opts.add("--disable-infobars");
        opts.add("--blink-settings=imagesEnabled=false"); // disables image ads
        opts.add("--disable-popup-blocking");
        opts.add("--disable-features=InterestCohortAPI,SameSiteByDefaultCookies,CookiesWithoutSameSiteMustBeSecure");
        opts.add("--disable-extensions");
        opts.add("--disable-notifications");
        opts.add("--no-default-browser-check");
        opts.add("--disable-features=PreloadMediaEngagementData,MediaRouter");

// Still experimental and may not always work:
        opts.add("--disable-features=DownloadBubble");
        opts.add("--disable-features=DownloadsUX");
        opts.add("--suppress-download-notification");

//        opts.add()
        return opts;
    }

    public  Map<String,Object> definePreferences(){
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
