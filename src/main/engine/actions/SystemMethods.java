package engine.actions;

import engine.reporters.Loggers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemMethods {

    public static void deleteDirectory(String path) {
        File directory = new File(path);
        try {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.stop();
            FileUtils.deleteDirectory(directory);
            context.start(); // Restart Log4j2 context
            Loggers.log.info("Deleted the directory {}", path);
        } catch (Exception e) {
            Loggers.log.info("couldn't delete directory {}", path);
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Loggers.log.warn("File does not exist: {}", path);
            return;
        }
        try {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            context.stop();
            FileUtils.delete(file);
                context.start();
            Loggers.log.info("Deleted the file: {}", path);
        } catch (Exception e) {
            Loggers.log.error("Couldn't delete file: {}. Error: {}", path, e.getMessage());
        }

    }

    public static void runFile(String path) {
        File file = new File(path);
        file.setExecutable(true);
        if (file.canExecute()) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(file.getAbsolutePath());
                Process process = processBuilder.start();
                process.waitFor();
                Loggers.log.info("File executed: {}",path);
            } catch (IOException | InterruptedException e) {
                Loggers.log.info("file isn't executed: {}",path);
            }
        } else {
            Loggers.log.info("file {} isn't executable type",path);
        }
    }

    public static boolean checkExistenceOfFile(String path){
        File file = new File(path);
        Loggers.log.info("Check if file exists {}", file.exists());
        return file.exists();
    }


    public static String readFileContent(String path){
                Path pth = Paths.get(path);
        String lines ="";
                try {
                     lines = String.valueOf(Files.readAllLines(pth));
                } catch (IOException ex) {
                    // handle exception...
                }
        Loggers.log.info("get file contents", lines);
                return lines;
        }

}