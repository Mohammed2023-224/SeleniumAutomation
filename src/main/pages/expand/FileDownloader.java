package pages.expand;

import engine.actions.ElementActions;
import engine.actions.SystemMethods;
import engine.actions.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class FileDownloader extends HomePage{

    private  By upload=By.id("fileSubmit");
    private  By fileInput=By.id("fileInput");
    private  By assertion=By.xpath("//h1[text()='File Uploaded!']");
    public FileDownloader(WebDriver driver) {
        super(driver);
    }

    public void downloadFile(){
        ElementActions.clickElement(driver, By.xpath("//a[@data-testid='cdct.jpg']"));
        Waits.waitForFileToBeDownloaded(driver,"C:\\Users\\USER\\Downloads\\cdct.jpg");
        Assert.assertTrue(SystemMethods.checkExistenceOfFile("C:\\Users\\USER\\Downloads\\cdct.jpg"));

    }

    public void fileUpload(){
        ElementActions.typeInElement(driver,fileInput,"C:\\Users\\USER\\Downloads\\cdct.jpg");
        ElementActions.clickElement(driver,upload);
        Waits.waitToBeVisible(driver,assertion);
        Assert.assertTrue(ElementActions.checkIfElementVisible(driver,assertion));
    }
}
