package pages.expand;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class DragAndDrop extends HomePage{
    private By columnA=By.id("column-a");
    private By columnB=By.id("column-b");
    private By redCircle=By.id("column-b");
    private By greenCircle=By.id("column-b");
    private By blueCircle=By.id("column-b");
    public DragAndDrop(WebDriver driver) {
        super(driver);
    }

    public void dragAToB(){
        ElementActions.dragAndDrop(driver,columnA,columnB);
        Assert.assertTrue(ElementActions.getText(driver,columnA).equalsIgnoreCase("b"));
    }

        public void circleDragging(){
        ElementActions.dragAndDrop(driver,columnA,columnB);
        Assert.assertTrue(ElementActions.getText(driver,columnA).equalsIgnoreCase("b"));
    }



}
