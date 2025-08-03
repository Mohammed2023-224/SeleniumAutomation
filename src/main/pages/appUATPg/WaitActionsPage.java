package pages.appUATPg;

import engine.actions.BrowserActions;
import engine.actions.ElementActions;
import engine.actions.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class WaitActionsPage {
private final WebDriver driver;


public WaitActionsPage(WebDriver driver){
    this.driver=driver;
}

private final By minWaitTime= By.id("min_wait");
    private final By maxWaitTime= By.id("max_wait");
    By alertTrigger=By.id("alert_trigger");
    By promptTrigger=By.id("prompt_trigger");
    By visibleTrigger=By.id("visibility_trigger");
    By visibilityTarget =By.id("visibility_target");
    By invisibleTrigger =By.id("invisibility_trigger");
    By invisibilityTarget =By.id("invisibility_target");
    By enabledTrigger =By.id("enabled_trigger");
    By enabledTarget=By.id("enabled_target");
    By titleTrigger=By.id("page_title_trigger");
    By textTrigger =By.id("text_value_trigger");
    By textTarget =By.id("wait_for_value");
    By frameTrigger =By.id("wait_for_frame");
    By frameButtonTarget =By.id("inner_button");
    By visibilityAssertionTarget = By.xpath("//h3[@class='popover-header']");
    By invisibilityAssertionTarget = By.xpath("//p[@id='spinner_gone']");
    By frame =By.id("frm");

    private By assertion(String id){
        return By.id(id);
    }
    private By h3Headers(String headers){
        return By.xpath("//h3[text()='"+headers+"']");
    }

    @Step("Type min wait [{wait}]")
    public void typeMinWait(String wait){
        ElementActions.clearField(driver,minWaitTime);
        ElementActions.typeInElement(driver,minWaitTime,wait);
    }

    @Step("Type max wait [{wait}]")
    public void typeMaxWait(String wait){
        ElementActions.clearField(driver,maxWaitTime);

        ElementActions.typeInElement(driver,maxWaitTime,wait);
    }

    @Step("Handling wait for alert")

    public void handleWaitForAlert(int maxTime){
        Waits.waitToBeVisible(driver,h3Headers("Wait for alert to be present"));
        ElementActions.clickElement(driver,alertTrigger);
        Waits.waitForAlert(driver,maxTime);
        BrowserActions.acceptAlert(driver);
        Assert.assertTrue(ElementActions.getText(driver,assertion("alert_handled_badge")).contains("Alert handled"));
        ElementActions.clickElement(driver,promptTrigger);
        Waits.waitForAlert(driver,maxTime);
        BrowserActions.acceptAlert(driver);
        Assert.assertTrue(ElementActions.getText(driver,assertion("confirm_ok")).contains("Confirm"));
    }


    @Step("Handling wait for visibility")
    public void handleWaitForVisibility(int maxTime){
        ElementActions.clickElement(driver,visibleTrigger);
        Waits.waitToBeVisible(driver, visibilityTarget);
        ElementActions.clickElement(driver, visibilityTarget);
        Waits.waitToBeVisible(driver,visibilityAssertionTarget);
        Assert.assertTrue(ElementActions.getText(driver,visibilityAssertionTarget)
                .contains("Can you see me?"));
    }

    @Step("Handling wait for invisibility")
    public void handleWaitForInvisibility(int maxTime){
        ElementActions.clickElement(driver, invisibleTrigger);
        Waits.waitToBeInvisible(driver, invisibilityTarget);
        Assert.assertTrue(ElementActions.getText(driver,invisibilityAssertionTarget)
                .contains("Thank God that spinner is gone!"));
    }


    @Step("Handling wait for enabled")
    public void handleWaitForEnabled(int maxTime){
        ElementActions.clickElement(driver, enabledTrigger);
        Waits.waitToBeClickable(driver,enabledTarget);
        Assert.assertTrue(ElementActions.getText(driver,enabledTarget)
                .contains("Enabled Button"));
    }

    @Step("Handling wait for title")
    public void handleWaitForTitle(int maxTime){
        ElementActions.clickElement(driver,titleTrigger);
        Waits.explicitWaitShortTime(driver).until(ExpectedConditions.titleContains("My New Title"));
        Assert.assertEquals(driver.getTitle(), ("My New Title!"));
    }

    @Step("Handling wait for text")
    public void handleWaitForText(int maxTime){
        ElementActions.clickElement(driver, textTrigger);
        Waits.explicitWaitShortTime(driver).until
                (ExpectedConditions.textToBePresentInElementValue(textTarget,"Dennis"));
    }

    @Step("Handling wait for frame")
    public void handleWaitForFrame(int maxTime){
        ElementActions.clickElement(driver, frameTrigger);
        Waits.explicitWaitLongTime(driver).until
                (ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
        System.out.println("test");
        ElementActions.clickUsingJavaScript(driver, frameButtonTarget);
        Assert.assertTrue(ElementActions.getText(driver, frameButtonTarget)
                .contains("Clicked"));
    }
}
