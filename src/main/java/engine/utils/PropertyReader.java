package engine.utils;


import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class PropertyReader {
    static Properties prop =new Properties();
    private static final Map<String, String> CACHE = new ConcurrentHashMap<>();
public static Properties readAllProperties() {
    String env = System.getProperty("env", "default");
    List<String> resourcePaths = List.of(
            "properties",
            "properties/" + env
    );
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    StringBuilder log = new StringBuilder();
    for (String basePath : resourcePaths) {
        try {
            Enumeration<URL> resources = cl.getResources(basePath);
            List<URL> urls = Collections.list(resources);

            urls.sort(Comparator.comparing(
                    u -> u.getProtocol().equals("jar") ? 0 : 1
            ));
            for (URL url : urls){
                log.append(url).append(" ");

                try {
                    if (url.getProtocol().equals("jar")) {
                        loadFromJar(url);
                    }
                    if (url.getProtocol().equals("file")) {
                        Path dir = Paths.get(url.toURI());
                        try (Stream<Path> paths = Files.list(dir)) {
                            loadPropertyFiles(paths);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException("Failed loading configs from " + url, e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan classpath: " + basePath, e);
        }
    }
    System.setProperty("readPropertyPath", log.toString());
    return prop;
}

    private static void loadPropertyFiles(Stream<Path> paths) {
        paths.filter(p -> p.toString().endsWith(".properties"))
                .forEach(p -> {
                    try (InputStream is = Files.newInputStream(p)) {
                        prop.load(is);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static void loadFromJar(URL dirUrl) throws IOException {
        String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {

            String baseEntry = dirUrl.getPath().substring(dirUrl.getPath().indexOf("!") + 2);

            jar.stream()
                    .filter(e -> !e.isDirectory())
                    .filter(e -> e.getName().startsWith(baseEntry))
                    .filter(e -> e.getName().endsWith(".properties"))
                    .forEach(e -> {
                        try (InputStream is = jar.getInputStream(e)) {
                            prop.load(is);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        }
    }

    private static String readProp(String key){
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
