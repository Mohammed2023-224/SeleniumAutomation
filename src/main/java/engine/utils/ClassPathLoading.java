package engine.utils;

import engine.reporters.Loggers;

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
                Loggers.getLogger().error("resource doesn't exist");
            }

            assert url != null;
            if ("jar".equals(url.getProtocol())) {
                return extractFromJar(url, resourcePath,executable);
            }

            if ("file".equals(url.getProtocol())) {
                return extractFromFilePath(url,executable);
            }
            Loggers.getLogger().error("Unsupported protocol: " + url.getProtocol());
            return  null;
        } catch (Exception e) {
            Loggers.getLogger().error("Failed to load resource: " + resourcePath, e);
            return  null;
        }
    }

    private static Path extractFromJar(URL url, String resourcePath,Boolean executable)  {
        String suffix = resourcePath.substring(resourcePath.lastIndexOf('.'));
        Path temp = null;
        try {
            temp = Files.createTempFile(resourcePath.replace('/', '_'), suffix);
        } catch (IOException e) {
            Loggers.getLogger().error("Can't read jar file",e);
            return null;
        }

            try (InputStream in = url.openStream()) {
                Files.copy(in, temp, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Loggers.getLogger().error("Can't read jar file",e);
        }
        if(Boolean.TRUE.equals(executable))       {
            Loggers.getLogger().info("set file executable to true {}",temp.toFile().setExecutable(true));}
        temp.toFile().deleteOnExit();
        return temp;
    }
    private static Path extractFromFilePath(URL url,Boolean executable)  {
        Path driverPath = null;
        try {
            driverPath = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            Loggers.getLogger().error("Can't read file",e);
            return null;
        }
        if(Boolean.TRUE.equals(executable)) {
        Loggers.getLogger().info("set file executable to true {}",driverPath.toFile().setExecutable(true));}
        return driverPath;
    }

    /* =========================================================
       MULTI-RESOURCE (folders, override logic)
       ========================================================= */

    public static void loadFromDirectories(
            List<String> classPathDirs,
            Consumer<InputStream> consumer
            ,String extension
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
                        loadFromJarDirectory(url, consumer,extension);
                    } else if ("file".equals(url.getProtocol())) {
                        loadFromFileDirectory(url, consumer,extension);
                    }
                }

            } catch (IOException e) {
                Loggers.getLogger().error("Failed scanning classpath: " + basePath, e);
            }
        }
    }

    private static void loadFromFileDirectory(URL url, Consumer<InputStream> consumer, String extension) {
        try {
            Path dir = Paths.get(url.toURI());

            try (Stream<Path> files = Files.list(dir)) {
                files.filter(p -> p.toString().endsWith(extension))
                        .forEach(p -> {
                            try (InputStream is = Files.newInputStream(p)) {
                                consumer.accept(is);
                            } catch (IOException e) {
                                Loggers.getLogger().error("Can't load directory",e);
                            }
                        });
            }

        } catch (Exception e) {
            Loggers.getLogger().error("Can't find directory",e);
        }
    }

    private static void loadFromJarDirectory(URL dirUrl, Consumer<InputStream> consumer,String extension )
            throws IOException {

        String jarPath = dirUrl.getPath().substring(5, dirUrl.getPath().indexOf("!"));
        String baseEntry = dirUrl.getPath()
                .substring(dirUrl.getPath().indexOf("!") + 2);

        try (JarFile jar = new JarFile(
                URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {

            jar.stream()
                    .filter(e -> !e.isDirectory())
                    .filter(e -> e.getName().startsWith(baseEntry))
                    .filter(e -> e.getName().endsWith(extension))
                    .forEach(e -> {
                        try (InputStream is = jar.getInputStream(e)) {
                            consumer.accept(is);
                        } catch (IOException ex) {
                            Loggers.getLogger().error("Can't load directory",ex);
                        }
                    });
        }
    }
}
