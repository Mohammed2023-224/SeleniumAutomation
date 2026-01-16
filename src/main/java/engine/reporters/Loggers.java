package engine.reporters;

import engine.constants.FrameworkConfigs;
import engine.utils.ClassPathLoading;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;


import java.nio.file.Path;


public class Loggers {

private static Logger logger;
    static {
        Path path = ClassPathLoading.getResourceAsPath("xml/log4j2.xml", false);
       String newPath=FrameworkConfigs.xmlFilesPath().isEmpty()?  path.toString():FrameworkConfigs.xmlFilesPath();
        Configurator.initialize(null,newPath);
    }
    public static Logger getLogger() {

        if (logger == null) {
            boolean perTestLog = FrameworkConfigs.perTestLog();
            logger = (Logger) (perTestLog ? LogManager.getRootLogger() : LogManager.getLogger("All_tests"));
            logger.info("Initialized logger where per test logger is "+ perTestLog);
            logger.info("Read all property files found at path "+System.getProperty("readPropertyPath")+ " where properties inside folders override outside properties");
        }
        return logger;
    }
}
