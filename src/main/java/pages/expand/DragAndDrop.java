package pages.expand;

import engine.actions.ElementActions;
import engine.actions.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class DragAndDrop extends HomePage{
    private By columnA=By.id("column-a");
    private By columnB=By.id("column-b");
    private By redCircle=By.className("red");
    private By greenCircle=By.className("green");
    private By blueCircle=By.className("blue");
    private By target=By.id("target");
    private By targetAssertion(String num){
     return By.xpath("//div[@id='target']//div["+num+"]");
    }
    public DragAndDrop(WebDriver driver) {
        super(driver);
    }

    public void dragAToB(){
        ElementActions.dragAndDrop(driver,columnA,columnB);
        Assert.assertTrue(ElementActions.getText(driver,columnA).equalsIgnoreCase("b"));
    }

        public void circleDragging(){
        ElementActions.dragAndDrop(driver,greenCircle,target);
            Waits.waitToBeVisible(driver,targetAssertion("1"));
        Assert.assertTrue(driver.findElement(targetAssertion("1")).getDomAttribute("class").equalsIgnoreCase("green"));
        ElementActions.dragAndDrop(driver,redCircle,target);
            Assert.assertTrue(driver.findElement(targetAssertion("2")).getDomAttribute("class").equalsIgnoreCase("red"));
            ElementActions.dragAndDrop(driver,blueCircle,target);
            Assert.assertTrue(driver.findElement(targetAssertion("3")).getDomAttribute("class").equalsIgnoreCase("blue"));
        }



}
