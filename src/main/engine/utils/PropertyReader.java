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
    static String path="src/main/resources/properties/";

    public static Properties readAllProperties(){
        prop=new Properties();
        ArrayList<String> files=new ArrayList<>();
        files.add(path+"Path.properties");
        files.add(path+"Links.properties");
        files.add(path+"Configs.properties");
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
