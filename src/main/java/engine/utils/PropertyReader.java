package engine.utils;


import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class PropertyReader {
    static Properties prop;
    static String path="src/test/resources/properties/";
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

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
                        } catch (IOException e) {
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config directory: " + configDir, e);
        }
        System.setProperty("readPropertyPath", path+"/"+env);
        return prop;
    }

    public static String readProp(String key){
        return prop.getProperty(key);
    }

    private static String resolve(String key) {
        return CACHE.computeIfAbsent(key, k -> {
            String sysValue = System.getProperty(k);
            if (sysValue != null && !sysValue.isBlank()) {
                return sysValue;
            }
            String fileValue = readProp(k);

            if (fileValue == null) {
                throw new IllegalStateException(
                        "Missing configuration key: " + k
                );
            }

            return fileValue.trim();
        });
    }
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

}
