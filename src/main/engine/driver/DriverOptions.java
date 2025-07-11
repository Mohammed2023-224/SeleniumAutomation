package engine.driver;

import engine.constants.Constants;
import engine.reporters.Loggers;
import engine.utils.JSONReader;

import java.util.ArrayList;

public class DriverOptions {
    static ArrayList<String> opts=new ArrayList<>();

    public static ArrayList<String> defineDriverOptions(){
        if(Constants.headlessMode.equals("true")){
            opts.add("--headless");
            Loggers.log.info("Activate Headless mode");
        }
        if(Constants.maximizedMode.equals("true")){
            opts.add("--maximized");
            Loggers.log.info("Start maximized");
        }
        if(Constants.popupBlocker.equals("true")){
            opts.add("--disable-popup-blocking");
            Loggers.log.info("disable popup blocking");
        }
        opts.add("--disable-blink-features=AutomationControlled");
        return opts;
    }

}
