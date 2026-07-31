package engine.utils.propertyFilesHandlers;

import engine.exceptions.CustomExceptions;
import engine.reporters.Loggers;
import engine.utils.ClassPathLoading;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertyLoader {

    private PropertyLoader(){}

    private static final Properties PROPERTIES = loadProperties();

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

    public static Map<String, String> loadAsMap(String filePath) {
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

        Map<String, String> propertyMap = new LinkedHashMap<>();
        properties.stringPropertyNames().forEach(key ->
                propertyMap.put(key, properties.getProperty(key))
        );

        return propertyMap;
    }
}
