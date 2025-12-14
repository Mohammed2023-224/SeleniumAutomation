package engine.utils;

import engine.reporters.Loggers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class PropertyReader {
    static Properties prop;
    static FileReader fr;
    static String path="src/test/resources/properties/";

    public static Properties readAllProperties(){
        String env= System.getProperty("env", "default");
        Path configDir = Paths.get(path, env);
        prop=new Properties();
        try (Stream<Path> paths = Files.list(configDir)) {
            paths
                    .filter(p -> p.toString().endsWith(".properties"))
                    .forEach(p -> {
                        try (FileReader fr = new FileReader(p.toFile())) {
                            prop.load(fr);
                            Loggers.log.info("Loaded properties file: {}", p.toFile());
                        } catch (IOException e) {
                            Loggers.log.error("Failed to load file {}", p, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config directory: " + configDir, e);
        }

        return prop;
    }

    public static String readProp(String key){
        return prop.getProperty(key);
    }

}
