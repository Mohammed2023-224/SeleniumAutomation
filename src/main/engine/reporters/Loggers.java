package engine.reporters;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Loggers {

    static {
        Configurator.initialize(null,"src/main/resources/xml/log4j2.xml");
    }
    private static Loggers instance; // Private static variable to hold the single instance

    public  Logger log = (Logger) LogManager.getRootLogger();

    public static Loggers getInstance() {
        if (instance == null) { // Lazy initialization: create instance only if it doesn't exist
            instance = new Loggers();
        }
        return instance;
    }

        public  void addInfoAndAllureStep(String logs){
        Allure.step(logs);
        log.info(logs);
    }

}
