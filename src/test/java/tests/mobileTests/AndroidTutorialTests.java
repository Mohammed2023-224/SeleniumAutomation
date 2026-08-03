package tests.mobileTests;

import engine.driver.androidDriver.AndroidDriverFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.appiumDemo.*;
import tests.baseClasses.BaseMobileTestClass;

public class AndroidTutorialTests extends BaseMobileTestClass {
HomeScreen homeScreen;
EnterSomeValueScreen enterSomeValueScreen;
ContacUsScreen contacUsScreen;
ScrollViewScreen scrollViewScreen;
TabViewScreen tabViewScreen;
LoginScreen loginScreen;
LongClickScreen longClickScreen;

    @Test
    public void enterValueTest(){
        homeScreen.clickOnLink(HomePageLinks.EnterSomeValues);
        enterSomeValueScreen.typeText("test");
        enterSomeValueScreen.clickSubmitButton();
        enterSomeValueScreen.assertPreview("test");
    }

    @Test
    public void contactUsTest(){
        homeScreen.clickOnLink(HomePageLinks.ContactUs);
        contacUsScreen.typeEmail("tesd");
        contacUsScreen.typeAddress("tesd");
        contacUsScreen.typeName("tesd");
        contacUsScreen.typeMobile("");
        contacUsScreen.clickSubmitButton();
        contacUsScreen.assertPreview("tesd","tesd","tesd","");
    }

    @Test
    public void scrollViewTest(){
        homeScreen.clickOnLink(HomePageLinks.ScrollView);
        scrollViewScreen.scrollToElement();
        scrollViewScreen.clickElement();
        scrollViewScreen.handleAlert();
        homeScreen.waitLinkVisibility(HomePageLinks.ScrollView);
    }

    @Test
    public void tabViewTest(){
        homeScreen.clickOnLink(HomePageLinks.TabView);
        tabViewScreen.clickTab("MOVIE");
        tabViewScreen.clickTab("SPORT");
        tabViewScreen.clickTab("HOME");
        tabViewScreen.asserTextView();
    }

    @Test
    public void loginTest(){
        homeScreen.clickOnLink(HomePageLinks.Login);
        loginScreen.typeEmail();
        loginScreen.typePassword();
        loginScreen.clickSubmitButton();
        loginScreen.typeAdmin();
        loginScreen.clickSubmitAdminButton();
        loginScreen.assertPreview();
    }

    @Test
    public void LongClickTest(){
        homeScreen.longClickLink(HomePageLinks.LongClick);
        longClickScreen.typeEmailInAlert();
    }

    @BeforeClass
    private void initClasses(){
        AppiumDriver driver=AndroidDriverFactory.getDriver();
        enterSomeValueScreen=new EnterSomeValueScreen(AndroidDriverFactory.getDriver());
        homeScreen=new HomeScreen(AndroidDriverFactory.getDriver());
        contacUsScreen=new ContacUsScreen(AndroidDriverFactory.getDriver());
        scrollViewScreen=new ScrollViewScreen(AndroidDriverFactory.getDriver());
        tabViewScreen=new TabViewScreen(driver);
        loginScreen=new LoginScreen(driver);
        longClickScreen=new LongClickScreen(driver);
    }
}
