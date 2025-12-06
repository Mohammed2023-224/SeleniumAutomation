package pages.expand;

import engine.actions.DragAndDropActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class DragAndDrop extends HomePage{
    private By columnA=By.id("column-a");
    private By columnB=By.id("column-b");
    private By redCircle=By.className("red");
    private By greenCircle=By.className("green");
    private By blueCircle=By.className("blue");
    private By target=By.id("target");
    private By targetAssertion(String num){
     return By.xpath("(//div[@id='target']//div)["+num+"]");
    }
    public DragAndDrop(WebDriver driver) {
        super(driver);
    }

    public void dragAToB(){
        DragAndDropActions.dragAndDrop(driver,columnA,columnB);
        Assert.assertTrue(ElementActions.getText(driver,columnA).equalsIgnoreCase("b"));
    }

        public void circleDragging() {
        int c=1;
            for (int i = 1; i < 3; i++) {
                try {
                    DragAndDropActions.dragAndDrop(driver, greenCircle, target);
                    Waits.waitToBeVisible(driver, targetAssertion(String.valueOf(i)));
                    Waits.fluentWaitShortTime(driver).until(ExpectedConditions.domAttributeToBe(
                            driver.findElement(targetAssertion(String.valueOf(i))), "class", "green"));
                    c=i;
                    break;
                } catch (Exception e) {
                        Loggers.log.warn("Drag-and-drop failed, retrying...");
                }
            }
                System.out.println(ElementActions.getElementAttribute(driver, targetAssertion(String.valueOf(c)), "class"));
                Assert.assertTrue(driver.findElement(targetAssertion(String.valueOf(c))).getDomAttribute("class").equalsIgnoreCase("green"));
            DragAndDropActions.dragAndDrop(driver, redCircle, target);
                Waits.fluentWaitShortTime(driver).until(ExpectedConditions.domAttributeToBe(
                        driver.findElement(targetAssertion(String.valueOf(c+1))), "class", "red"));
                Assert.assertTrue(driver.findElement(targetAssertion(String.valueOf(c+1))).getDomAttribute("class").equalsIgnoreCase("red"));
            DragAndDropActions.dragAndDrop(driver, blueCircle, target);
                Waits.fluentWaitShortTime(driver).until(ExpectedConditions.domAttributeToBe(
                        driver.findElement(targetAssertion(String.valueOf(c+2))), "class", "blue"));
                Assert.assertTrue(driver.findElement(targetAssertion(String.valueOf(c+2))).getDomAttribute("class").equalsIgnoreCase("blue"));

        }}

