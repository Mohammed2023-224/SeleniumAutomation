package engine.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ClassPathLoading {

    private ClassPathLoading() {}

    /* =========================================================
       SINGLE RESOURCE (driver, log4j, certs, etc.)
       ========================================================= */

    public static Path getResourceAsPath(String resourcePath,Boolean executable) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(resourcePath);
            if (url == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            if ("jar".equals(url.getProtocol())) {
                return extractFromJar(url, resourcePath,executable);
            }

            if ("file".equals(url.getProtocol())) {
                return Paths.get(url.toURI());
            }

            throw new RuntimeException("Unsupported protocol: " + url.getProtocol());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load resource: " + resourcePath, e);
        }
    }

    private static Path extractFromJar(URL url, String resourcePath,Boolean executable) throws IOException {
        String suffix = resourcePath.substring(resourcePath.lastIndexOf('.'));
        Path temp = Files.createTempFile(resourcePath.replace('/', '_'), suffix);
        try (InputStream in = url.openStream()) {
            Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
        }
        if(executable) temp.toFile().setExecutable(true);
        temp.toFile().deleteOnExit();
        return temp;
    }
    private static Path extractFromFilePath(URL url,Boolean executable) throws IOException {
        Path driverPath = null;
        try {
            driverPath = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if(executable) driverPath.toFile().setExecutable(true);
        return driverPath;
    }

    /* =========================================================
       MULTI-RESOURCE (folders, override logic)
       ========================================================= */

    public static void loadFromDirectories(
            List<String> classPathDirs,
            Consumer<InputStream> consumer
    ) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        for (String basePath : classPathDirs) {
            try {
                Enumeration<URL> resources = cl.getResources(basePath);
                List<URL> urls = Collections.list(resources);

                // 🔥 JAR FIRST, FILE LAST → override works
                urls.sort(Comparator.comparing(u ->
                        "jar".equals(u.getProtocol()) ? 0 : 1
                ));

                for (URL url : urls) {
                    if ("jar".equals(url.getProtocol())) {
                        loadFromJarDirectory(url, consumer);
                    } else if ("file".equals(url.getProtocol())) {
                        loadFromFileDirectory(url, consumer);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException("Failed scanning classpath: " + basePath, e);
            }
        }
    }

    private static void loadFromFileDirectory(URL url, Consumer<InputStream> consumer) {
        try {
            Path dir = Paths.get(url.toURI());

            try (Stream<Path> files = Files.list(dir)) {
                files.filter(p -> p.toString().endsWith(".properties"))
                        .forEach(p -> {
                            try (InputStream is = Files.newInputStream(p)) {
                                consumer.accept(is);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadFromJarDirectory(URL dirUrl, Consumer<InputStream> consumer)
            throws IOException {

        String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
        String baseEntry = dirUrl.getPath()
                .substring(dirUrl.getPath().indexOf("!") + 2);

        try (JarFile jar = new JarFile(
                URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {

            jar.stream()
                    .filter(e -> !e.isDirectory())
                    .filter(e -> e.getName().startsWith(baseEntry))
                    .filter(e -> e.getName().endsWith(".properties"))
                    .forEach(e -> {
                        try (InputStream is = jar.getInputStream(e)) {
                            consumer.accept(is);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        }
    }
}
