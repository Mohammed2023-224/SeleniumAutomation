package engine.reporters;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Loggers {

    public static Logger log = (Logger) LogManager.getRootLogger();
}
