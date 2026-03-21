package engine.reporters;

import engine.constants.FrameworkConfigs;
import engine.utils.ClassPathLoading;
import engine.utils.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import java.nio.file.Path;

public class Loggers {
private Loggers(){}
    private static Logger logger;
    private static  Logger allTestlogger =  (Logger) LogManager.getLogger("All_tests");
    private static final boolean PER_TEST_LOG = FrameworkConfigs.perTestLog();

    static {
        Path path = ClassPathLoading.getResourceAsPath(PropertyReader.get("logFile", String.class), false);
       String newPath;
        if (FrameworkConfigs.xmlFilesPath().isEmpty()) {
            assert path != null;
            newPath = path.toString();
        } else {
            newPath = FrameworkConfigs.xmlFilesPath();
        }
        Configurator.initialize(null,newPath);
    }

    public static void logInfo(String info){
        allTestlogger.info(info);
        String testFileName = ThreadContext.get("testLogFileName");
        if (PER_TEST_LOG && testFileName != null && !testFileName.isEmpty()) {
            Logger testLogger = DynamicLoggers.getTestLogger(testFileName);
            testLogger.info(info);
        }
        }

        public static void logDebug(String info){
        allTestlogger.debug(info);
        String testFileName = ThreadContext.get("testLogFileName");
            if (PER_TEST_LOG && testFileName != null && !testFileName.isEmpty()) {
                Logger testLogger = DynamicLoggers.getTestLogger(testFileName);
                testLogger.debug(info);
            }
        }

    public static void logError(String info){
        allTestlogger.error(info);
        String testFileName = ThreadContext.get("testLogFileName");
        if (PER_TEST_LOG && testFileName != null && !testFileName.isEmpty()) {
            Logger testLogger = DynamicLoggers.getTestLogger(testFileName);
            testLogger.error(info);
        }
    }

    public static void logWarn(String info){
        allTestlogger.warn(info);
        String testFileName = ThreadContext.get("testLogFileName");
        if (PER_TEST_LOG && testFileName != null && !testFileName.isEmpty()) {
            Logger testLogger = DynamicLoggers.getTestLogger(testFileName);
            testLogger.warn(info);
        }
    }

    public static void cleanupPerTestAppender(String testFileName) {
        if (testFileName != null && !testFileName.isEmpty()) {
            DynamicLoggers.removeAppenderForTest(testFileName);
        }
    }
}
