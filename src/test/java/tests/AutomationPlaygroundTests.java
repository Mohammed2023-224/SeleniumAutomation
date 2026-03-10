package tests;

import pages.appUATPg.*;
import tests.baseTest.BaseTestClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AutomationPlaygroundTests extends BaseTestClass {
HomePage homePage;
WaitActionsPage waitActionsPage;
PopUpWindows popUpWindows;
Frames frames;
MouseActions mouseActions;
KeyboardActions keyboardActions;
FormPage formPage;
SamplePage samplePage;
AdvancedUI advancedUI;

    @Test()
    public void waitActionsTest() {
        int minWait=2; int maxWait=4;
        homePage.clickPage("Wait Conditions");
    waitActionsPage.typeMinWait(String.valueOf(minWait));
    waitActionsPage.typeMaxWait(String.valueOf(maxWait));
    waitActionsPage.handleWaitForAlert(maxWait);
    waitActionsPage.handleWaitForVisibility();
waitActionsPage.handleWaitForInvisibility();
waitActionsPage.handleWaitForEnabled();
waitActionsPage.handleWaitForTitle();
waitActionsPage.handleWaitForText();
waitActionsPage.handleWaitForFrame();
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

    @Test()
    public void mouseActionsTest() {
        homePage.clickPage("Mouse Actions");
        mouseActions.mouseActions();
    mouseActions.hovering();
    mouseActions.dragAndDrop();
    }

    @Test()
    public void keyboardActions() {
        homePage.clickPage("Keyboard Actions");
        keyboardActions.handleKeyboard();
    }

    @Test()
    public void formPageTests() {
        homePage.clickPage("Forms");
        formPage.userFillsExperience("6");
        formPage.userFillsProgrammingLanguagesCheckboxes();
        formPage.userFillsToolsRadioButtons();
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
