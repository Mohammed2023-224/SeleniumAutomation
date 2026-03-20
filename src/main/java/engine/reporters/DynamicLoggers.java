package engine.reporters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicLoggers {

    private DynamicLoggers(){}

    public static void stopAppenderRootLog(String appenderName) {
        LoggerContext ctx;
        Configuration config;
        Appender appender;
        ctx = (LoggerContext) LogManager.getContext(false);
        config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig("");
        loggerConfig.removeAppender(appenderName);
        appender = config.getAppender(appenderName);
        if (appender != null) {
            appender.stop();
        }
        ctx.updateLoggers();
    }

    public static void reconfigureLogs() {
        LoggerContext ctx;
        ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
    private static final String BASE_PATH = "reports/log4j/perTest/";
    private static final String PATTERN = "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n";
    private static final ConcurrentHashMap<String, LoggerConfig> activeLoggerConfigs = new ConcurrentHashMap<>();

    public static void addAppenderForTest(String testFileName) {
        if (activeLoggerConfigs.containsKey(testFileName)) {
            return;
        }
        try {
            File dir = new File(BASE_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();

            Layout layout = PatternLayout.newBuilder()
                    .setPattern(PATTERN)
                    .setConfiguration(config)
                    .build();

            String appenderName = "PerTest-" + testFileName;
            FileAppender appender = FileAppender.newBuilder()
                    .setName(appenderName)
                    .setFileName(BASE_PATH + testFileName + ".log")
                    .setLayout(layout)
                    .setAppend(false)
                    .setImmediateFlush(true)
                    .setConfiguration(config)
                    .build();

            appender.start();
            config.addAppender(appender);
            // Create a NEW logger for this test, not the same perTest logger
            String loggerName = "perTest-" + testFileName;
            LoggerConfig testLoggerConfig = new LoggerConfig(loggerName, org.apache.logging.log4j.Level.INFO, false);
            testLoggerConfig.addAppender(appender, null, null);
            config.addLogger(loggerName, testLoggerConfig);
            ctx.updateLoggers();
            activeLoggerConfigs.put(testFileName, testLoggerConfig);
        } catch (Exception e) {
            Loggers.logError("Failed to add appender for: " + testFileName);
        }
    }

    /**
     * Remove appender for a specific test
     */
    public static void removeAppenderForTest(String testFileName) {
        LoggerConfig testLoggerConfig = activeLoggerConfigs.remove(testFileName);
        if (testLoggerConfig != null) {
            try {
                LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
                Configuration config = ctx.getConfiguration();
                config.getLoggerConfig(testLoggerConfig.getName());
                config.removeLogger(testLoggerConfig.getName());
                for (Appender appender : testLoggerConfig.getAppenders().values()) {
                    appender.stop();
                    config.getAppenders().remove(appender.getName());
                }
                ctx.updateLoggers();
            } catch (Exception e) {
                Loggers.logError("Error removing appender "+ e.toString());
            }
        }
    }

    public static boolean hasAppender(String testFileName) {
        return activeLoggerConfigs.containsKey(testFileName);
    }

    public static Logger getTestLogger(String testFileName) {
        return (Logger) LogManager.getLogger("perTest-" + testFileName);
    }
}