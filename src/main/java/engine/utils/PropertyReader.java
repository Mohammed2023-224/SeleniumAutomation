package engine.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class PropertyReader {
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
                            throw new RuntimeException(e);
                        }
                    },".properties"
            );

            System.setProperty("readPropertyPath", resourcePaths.toString());
            return properties;
        }


        public static Properties readAllProperties() {
            return PropertyHolder.PROPERTIES;  // This triggers lazy initialization
        }

        private static void loadPropertyFiles(Stream<Path> paths, Properties targetProps) {
            paths.filter(p -> p.toString().endsWith(".properties"))
                    .forEach(p -> {
                        try (InputStream is = Files.newInputStream(p)) {
                            targetProps.load(is);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        // Modified to accept Properties parameter
        private static void loadFromJar(URL dirUrl, Properties targetProps) throws IOException {
            String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
            try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                String baseEntry = dirUrl.getPath().substring(dirUrl.getPath().indexOf("!") + 2);

                jar.stream()
                        .filter(e -> !e.isDirectory())
                        .filter(e -> e.getName().startsWith(baseEntry))
                        .filter(e -> e.getName().endsWith(".properties"))
                        .forEach(e -> {
                            try (InputStream is = jar.getInputStream(e)) {
                                targetProps.load(is);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
            }
        }

        // Convenience method if you need the Properties object
        public static Properties reedAll() {
            return readAllProperties();
        }


    }
}
