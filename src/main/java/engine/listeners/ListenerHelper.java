package engine.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class ListenerHelper {


    public static void stopAppenderRootLog(String appenderName){
        LoggerContext  ctx;
        Configuration config;
        Appender appender;
        ctx = (LoggerContext) LogManager.getContext(false);
        config=ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig("");
        loggerConfig.removeAppender(appenderName);
        appender = config.getAppender(appenderName);
        if (appender != null) {
            appender.stop();
        }
        ctx.updateLoggers();
    }

    public static void reconfigureLogs(){
        LoggerContext  ctx;
        ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
    }
}
