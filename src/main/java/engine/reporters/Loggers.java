package engine.reporters;

import engine.constants.FrameworkConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Loggers {

private static Logger logger;
    static {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL driverUrl = cl.getResource("xml/log4j2.xml");
            if (driverUrl == null) {
                throw new RuntimeException("log4j2.xml not found in resources");
            }
            if (driverUrl.getProtocol().equals("jar")) {
                try (InputStream in = driverUrl.openStream()) {
                    Path tempFile = Files.createTempFile("log4j2", ".xml");
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    Configurator.initialize(null,tempFile.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Path driverPath = Paths.get(driverUrl.toURI());
                File driverFile = driverPath.toFile();
                Configurator.initialize(null,driverFile.getAbsolutePath());

            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        Configurator.initialize(null,FrameworkConfigs.xmlFilesPath()+"log4j2.xml");
    }
    public static Logger getLogger() {

        if (logger == null) {
            boolean perTestLog = FrameworkConfigs.per_test_log();
            logger = (Logger) (perTestLog ? LogManager.getRootLogger() : LogManager.getLogger("All_tests"));
            logger.info("Initialized logger where per test logger is "+ perTestLog);
            logger.info("Read all property files found at path "+System.getProperty("readPropertyPath")+ " where properties inside folders override outside properties");
        }
        return logger;
    }
}
