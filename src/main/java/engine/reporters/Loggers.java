package engine.reporters;

import engine.constants.FrameworkConfigs;
import engine.utils.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Loggers {

private static Logger logger;
    static {
        Configurator.initialize(null,"src/main/resources/xml/log4j2.xml");
        PropertyReader.readAllProperties();
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
