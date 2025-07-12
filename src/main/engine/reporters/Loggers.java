package engine.reporters;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Loggers {

    static {
        Configurator.initialize(null,"src/main/resources/xml/log4j2.xml");
    }
    public static  Logger log = (Logger) LogManager.getRootLogger();

    public static void addInfoAndAllureStep(String logs){
        Allure.step(logs);
        log.info(logs);
    }

}
