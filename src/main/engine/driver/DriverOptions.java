package engine.driver;

import engine.reporters.Loggers;
import engine.utils.Configuration;
import engine.utils.JSONReader;

import java.util.ArrayList;
import java.util.List;

public class DriverOptions {
    static ArrayList<String> opts=new ArrayList<>();

    public static ArrayList<String> defineDriverOptions(){
        Configuration con= JSONReader.deserializeJson("src/main/resources/configuration.json");
        Configuration.Config configs=con.getUserList().get(0);
        if(configs.getHeadless().equals("true")){
            opts.add("--headless");
            Loggers.log.info("Activate Headless mode");
        }
        if(configs.getMaximized().equals("true")){
            opts.add("--maximized");
            Loggers.log.info("Start maximized");
        }
        opts.add("--disable-blink-features=AutomationControlled");
        opts.add("--disable-popup-blocking");
        return opts;
    }

}
