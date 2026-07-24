package engine.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Properties;

public class PropertyFileCreation {
    public static void createPropertyFile(LinkedHashMap<String,String> map ,
                                          String path,String fileName, String header){
        Properties props = new Properties();
        props.putAll(map);
        Path dir = Paths.get(path);
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (OutputStream os = Files.newOutputStream(dir.resolve(fileName+".properties"))) {
            props.store(os, header);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}