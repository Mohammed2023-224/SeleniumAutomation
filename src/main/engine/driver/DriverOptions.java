package engine.driver;

import java.util.ArrayList;

public class DriverOptions {
    static ArrayList<String> opts=new ArrayList<>();

    public static ArrayList<String> defineDriverOptions(){
        if("headless".equalsIgnoreCase("headless")){
//            opts.add("--headless");
        }
        return opts;
    }

}
