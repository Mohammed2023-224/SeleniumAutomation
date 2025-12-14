package testNG;

import pages.appUATPg.*;
import testNG.base.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AutomationPlaygroundTests extends BaseTest {
HomePage homePage;
WaitActionsPage waitActionsPage;
PopUpWindows popUpWindows;
Frames frames;
MouseActions mouseActions;
KeyboardActions keyboardActions;
FormPage formPage;
SamplePage samplePage;
AdvancedUI advancedUI;

    @Test(enabled = false)
    public void waitActionsTest() {
        int minWait=2; int maxWait=4;
        homePage.clickPage("Wait Conditions");
    waitActionsPage.typeMinWait(String.valueOf(minWait));
    waitActionsPage.typeMaxWait(String.valueOf(maxWait));
    waitActionsPage.handleWaitForAlert(maxWait);
    waitActionsPage.handleWaitForVisibility(maxWait);
waitActionsPage.handleWaitForInvisibility(maxWait);
waitActionsPage.handleWaitForEnabled(maxWait);
waitActionsPage.handleWaitForTitle(maxWait);
waitActionsPage.handleWaitForText(maxWait);
waitActionsPage.handleWaitForFrame(maxWait);
    }

    @Test
    public void popUpWindowsTest() {
        homePage.clickPage("Popup Windows");
        popUpWindows.handlePopups();
        }
    @Test
    public void framesTest() {
        homePage.clickPage("Frames");
        frames.handleFrames();
    }

    @Test(enabled = false)
    public void mouseActionsTest() {
        homePage.clickPage("Mouse Actions");
        mouseActions.mouseActions();
    mouseActions.hovering();
    mouseActions.dragAndDrop();
    }

    @Test(enabled = false)
    public void keyboardActions() {
        homePage.clickPage("Keyboard Actions");
        keyboardActions.handleKeyboard();
    }

    @Test(enabled = false)
    public void formPageTests() {
        homePage.clickPage("Forms");
        formPage.handleForm();
    }

    @Test()
    public void samplePageTest() {
        homePage.clickPage("Sample Pages");
        samplePage.newUser();
        samplePage.loginForm();
        samplePage.pizzaForm();
    }

    @Test()
    public void advancedUITests() {
        homePage.clickPage("Advanced UI Features");
        advancedUI.handleAdvancedUIChallenge();
    }

@BeforeMethod()
public void navigateToHomePage(){
    homePage.navigateHomePage();
}

    @BeforeClass
    private void initClasses(){
        popUpWindows=new PopUpWindows(driver);
        homePage=new HomePage(driver);
        waitActionsPage=new WaitActionsPage(driver);
        frames=new Frames(driver);
        mouseActions=new MouseActions(driver);
        keyboardActions=new KeyboardActions(driver);
        formPage=new FormPage(driver);
        samplePage=new SamplePage(driver);
        advancedUI=new AdvancedUI(driver);
    }
}
