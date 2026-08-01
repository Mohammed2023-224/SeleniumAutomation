package engine.utils.propertyFilesHandlers;

import engine.exceptions.CustomExceptions;
import engine.reporters.Loggers;
import engine.utils.ClassPathLoading;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class PropertyLoader {

    private PropertyLoader(){}

    private static final Properties PROPERTIES = loadProperties();

    /**
     *  reads all property files from target output that exists in folders properties or properties/environment passed or default folder
     * @return properties
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        String env = System.getProperty("env", "default");
        List<String> resourcePaths = List.of(
                "properties",
                "properties/" + env
        );
        ClassPathLoading.loadFromDirectories(
                resourcePaths,
                is -> {
                    try {
                        properties.load(is);
                    } catch (IOException e) {
                        Loggers.logError("Failed loading property file "+ e.getMessage() +is.toString());
                        throw new CustomExceptions("Failed to load properties: " + e.getMessage());
                    }
                },".properties"
        );
        System.setProperty("readPropertyPath", resourcePaths.toString());
        return properties;
    }

    public static Properties getAllProperties() {
        return PROPERTIES;
    }

    /**
     *
     * @param filePath: File path as a string
     * @return map of all the file data
     */
    public static Map<String, Object> loadAsMap(String filePath) {
        Path path = ClassPathLoading.getResourceAsPath(filePath, false);
        if (path == null) {
            throw new IllegalArgumentException("Properties file not found: " + filePath);
        }
        Properties properties = new Properties();
        try (InputStream is = Files.newInputStream(path)) {
            properties.load(is);
        } catch (IOException e) {
            throw new CustomExceptions("Failed to load properties: " + filePath, e);
        }
        Map<String, Object> propertyMap = new LinkedHashMap<>();
        properties.stringPropertyNames().forEach(key -> {
            String mpValue=properties.getProperty(key);
            Map<String,String> mpMap=new HashMap<>();
            if (mpValue.contains("=")) {
               mpMap= PropertyParser.parseValue(key,mpValue,Map.class);
                propertyMap.put(key, mpMap);
            }
            else {
                propertyMap.put(key, mpValue);
            }
        });

        return propertyMap;
    }
}
