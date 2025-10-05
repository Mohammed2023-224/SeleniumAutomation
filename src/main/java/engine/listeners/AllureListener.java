package engine.listeners;

import engine.reporters.Loggers;
import io.qameta.allure.Allure;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.v137.page.Page;

import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

public class AllureListener {

    public static void saveTextLog(String name, String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Allure.addAttachment(name,fis);
         Loggers.log.info("attached to report {}", filePath);
        } catch (FileNotFoundException e) {
         Loggers.log.warn("File can't be attached to report: {}", filePath);
        } catch (IOException e) {
         Loggers.log.warn("Error closing file stream for AllureListener: {}", filePath);
}
    }

    public static void  saveScreensShot(WebDriver driver, String text){
     Loggers.log.info("Taking screenshot and saving to allure report");
        io.qameta.allure.Allure.addAttachment(text, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

    }
