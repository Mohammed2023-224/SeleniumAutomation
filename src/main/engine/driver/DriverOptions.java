package engine.driver;

import engine.reporters.Loggers;

import java.util.ArrayList;

public class DriverOptions {
    static ArrayList<String> opts=new ArrayList<>();

    public static ArrayList<String> defineDriverOptions(){
        if("headless".equalsIgnoreCase("headless")){
//            opts.add("--headless");
            Loggers.log.info("Activate Headless mode");
        }
        if("maximized".equalsIgnoreCase("maximized")){
//            opts.add("--maximized");
            Loggers.log.info("Start maximized");
        }
        return opts;
    }

}
