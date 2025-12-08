package engine.utils;

import engine.reporters.Loggers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyReader {
    static Properties prop;
    static FileReader fr;
    static String path="src/test/resources/properties/qa/";
    static String devPath="src/test/resources/properties/dev/";

    public static Properties readAllProperties(){
        String env= System.getProperty("env", "qa");
        String configPath= env.equalsIgnoreCase("qa")? path:devPath;
        prop=new Properties();
        ArrayList<String> files=new ArrayList<>();
        files.add(configPath+"Path.properties");
        files.add(configPath+"Links.properties");
        files.add(configPath+"Configs.properties");
        for (String f: files) {
            try {
                fr = new FileReader(f);
                prop.load(fr);
            } catch (FileNotFoundException e) {
             Loggers.log.error("Can't find file {}", f);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return prop;
    }

    public static String readProp(String key){
        return prop.getProperty(key);
    }

}
