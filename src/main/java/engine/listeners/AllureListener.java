package engine.listeners;

import engine.reporters.Loggers;
import io.qameta.allure.Allure;

import io.qameta.allure.Step;
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
//@Step("Adding screen shot")
    public static void  saveScreensShot(WebDriver driver, String name){
        Allure.step( "Save screen shot",()-> {
            try {
                if (driver == null) return;
                byte[] ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Loggers.log.warn("Screen shot taken");
                Allure.addAttachment(name, "img/png", new ByteArrayInputStream(ss), ".png");
                Loggers.log.warn("attached to report");

            } catch (Exception e) {
                Loggers.log.warn("⚠ Unable to capture screenshot for Allure: {}", e.getMessage());
            }
        } );
    }

    }
