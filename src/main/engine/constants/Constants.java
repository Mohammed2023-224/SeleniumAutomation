package engine.constants;

import engine.utils.PropertyReader;

public class Constants {
     static {
          PropertyReader.readAllProperties();
     }

     public static int shortWaitTime= Integer.parseInt(PropertyReader.readProp("shortWaitTime"));
     public static int longWaitTime=Integer.parseInt(PropertyReader.readProp("longWaitTime"));
     public static String openAllure=PropertyReader.readProp("openAllureAfterTest");
     public static  String allureFile=PropertyReader.readProp("allure_bat_file_path");
     public static  String reportsPath=PropertyReader.readProp("report_logs_path");
     public static String url=PropertyReader.readProp("mainurl");
     public static String headlessMode=PropertyReader.readProp("headless");
     public static String maximizedMode=PropertyReader.readProp("maximized");
     public static String popupBlocker=PropertyReader.readProp("popup_blocker");

}
