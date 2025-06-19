package engine.reporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Loggers {

    static {
        Configurator.initialize(null,"src/main/resources/xml/log4j2.xml");
    }
    public static  Logger log = (Logger) LogManager.getLogger("logs");

}
