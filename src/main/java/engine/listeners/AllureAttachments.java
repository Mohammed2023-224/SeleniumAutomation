package engine.listeners;

import engine.reporters.Loggers;
import io.qameta.allure.Allure;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


import java.io.*;


public class AllureAttachments {

    public static void saveTextLog(String name, String filePath) {
        Allure.step( "Save text log",()-> {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Allure.addAttachment(name,fis);
         Loggers.log.info("attached to report {}", filePath);
        } catch (FileNotFoundException e) {
         Loggers.log.warn("File can't be found at: {}", filePath);
        } catch (IOException e) {
            Loggers.log.warn("Failed attaching log file: {}", filePath, e);
        }
    });
    }

    public static void  saveScreensShot(WebDriver driver, String name){
        Allure.step( "Save screen shot",()-> {
            try {
                if (driver == null) return;
                byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Loggers.log.info("Screen shot taken");
                Allure.addAttachment(name, "img/png", new ByteArrayInputStream(ss), ".png");
                Loggers.log.info("Screen shot attached to report");

            } catch (Exception e) {
                Loggers.log.warn("⚠ Unable to capture screenshot for Allure: {}", e.getMessage());
            }
        } );
    }

    }
