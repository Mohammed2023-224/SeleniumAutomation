package pages.appUATPg;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class KeyboardActions {
private final WebDriver driver;
    By typingArea= By.id("area");
    By currentKey= By.id("key");
    By keyCode= By.id("code");
    By shiftModifier= By.id("shift");
public KeyboardActions(WebDriver driver){
    this.driver=driver;
}
public void handleKeyboard(){

    ElementActions.typeInElement(driver,typingArea,"t");
    Assert.assertTrue(ElementActions.getText(driver,currentKey).contains("t"));
    Assert.assertTrue(ElementActions.getText(driver,keyCode).contains("KeyT"));
    ElementActions.typeInElement(driver,typingArea,"s");
    Assert.assertTrue(ElementActions.getText(driver,currentKey).contains("s"));
    Assert.assertTrue(ElementActions.getText(driver,keyCode).contains("KeyS"));
    ElementActions.typeInElement(driver,typingArea,"G");
    Assert.assertTrue(ElementActions.getText(driver,currentKey).contains("G"));
    Assert.assertTrue(ElementActions.getText(driver,keyCode).contains("KeyG"));
    Assert.assertTrue(ElementActions.getText(driver,shiftModifier).contains("Shift"));
    ElementActions.typeInElement(driver,typingArea,"5");
    Assert.assertTrue(ElementActions.getText(driver,currentKey).contains("5"));
    Assert.assertTrue(ElementActions.getText(driver,keyCode).contains("Digit5"));
    ElementActions.typeInElement(driver,typingArea," ");
    Assert.assertTrue(ElementActions.getText(driver,currentKey).contains(""));
    Assert.assertTrue(ElementActions.getText(driver,keyCode).contains("Space"));
    ElementActions.pressKeyboardKeys(driver,typingArea, Keys.ARROW_UP);
    Assert.assertTrue(ElementActions.getText(driver,currentKey).contains("ArrowUp"));
    Assert.assertTrue(ElementActions.getText(driver,keyCode).contains("ArrowUp"));
}

}
