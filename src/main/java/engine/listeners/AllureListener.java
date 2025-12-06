package engine.listeners;

import engine.reporters.Loggers;
import io.qameta.allure.Allure;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


import java.io.*;


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

    public static void  saveScreensShot(WebDriver driver, String name){
        try {
            if (driver == null) return;
            byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, new ByteArrayInputStream(ss));

        } catch (Exception e) {
            Loggers.log.warn("⚠ Unable to capture screenshot for Allure: {}", e.getMessage());
        }
    }

    }
