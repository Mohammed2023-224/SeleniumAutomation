package engine.utils;


import engine.reporters.Loggers;

import java.io.IOException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class PropertyReader {
    private PropertyReader(){}
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();
    public static <T> T get(String key, Class<T> type) {
        String value = resolve(key);
        if (value == null) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        try {
            if (type == String.class) {
                return type.cast(value);
            }
            if (type == Integer.class) {
                return type.cast(Integer.parseInt(value));
            }
            if (type == Boolean.class) {
                return type.cast(Boolean.parseBoolean(value));
            }
            if (type == Long.class) {
                return type.cast(Long.parseLong(value));
            }
            if (type == Double.class) {
                return type.cast(Double.parseDouble(value));
            }
            if (type == List.class) {
                return type.cast(Arrays.asList(value.split(",")));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to parse config key '" + key +
                            "' as " + type.getSimpleName() +
                            " (value=" + value + ")",
                    e
            );
        }
        throw new IllegalArgumentException(
                "Unsupported type: " + type.getName()
        );
    }
    private static String resolve(String key) {
        return CACHE.computeIfAbsent(key, k -> {
            String sysValue = System.getProperty(k);
            if (sysValue != null && !sysValue.isBlank()) {
                return sysValue;
            }

            String fileValue = PropertyHolder.readAllProperties().getProperty(k);

            if (fileValue == null) {
                throw new IllegalStateException(
                        "Missing configuration key: " + k
                );
            }

            return fileValue.trim();
        });
    }

    public static void clearCache() {
        CACHE.clear();
    }

    private static class PropertyHolder {
        static final Properties PROPERTIES = loadProperties();

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
                            Loggers.getLogger().error("Can't load file ");
                        }
                    },".properties"
            );

            System.setProperty("readPropertyPath", resourcePaths.toString());
            return properties;
        }


        public static Properties readAllProperties() {
            return PropertyHolder.PROPERTIES;  // This triggers lazy initialization
        }

        public static Properties reedAll() {
            return readAllProperties();
        }


    }
}
