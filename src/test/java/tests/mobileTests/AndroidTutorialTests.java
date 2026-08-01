package tests.mobileTests;

import engine.driver.androidDriver.AndroidDriverFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.appiumDemo.EnterSomeValueScreen;
import pages.appiumDemo.HomePageLinks;
import pages.appiumDemo.HomeScreen;
import tests.baseClasses.BaseMobileTestClass;

public class AndroidTutorialTests extends BaseMobileTestClass {
HomeScreen homeScreen;
EnterSomeValueScreen enterSomeValueScreen;

    @Test
    public void enterValueTest(){
        homeScreen.clickOnLink(HomePageLinks.EnterSomeValues);
        enterSomeValueScreen.typeText("test");
        enterSomeValueScreen.clickSubmitButton();
        enterSomeValueScreen.assertPreview("test");
    }

    @BeforeClass
    private void initClasses(){
        enterSomeValueScreen=new EnterSomeValueScreen(AndroidDriverFactory.getDriver());
        homeScreen=new HomeScreen(AndroidDriverFactory.getDriver());
    }
}
