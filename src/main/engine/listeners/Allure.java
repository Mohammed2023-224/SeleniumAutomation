package engine.listeners;

import engine.reporters.Loggers;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Allure {

    public static void  saveTextLog(String filePath){
        try {
            FileInputStream fis = new FileInputStream(filePath);
            io.qameta.allure.Allure.addAttachment("test logs", fis);
        } catch (FileNotFoundException e) {
            Loggers.log.warn("File can't be attached to report");
        }
    }

    public static void  saveScreensShot(WebDriver driver, String text){
        Loggers.log.info("Taking screenshot and saving to allure report");
        io.qameta.allure.Allure.addAttachment(text, new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
    }

}
