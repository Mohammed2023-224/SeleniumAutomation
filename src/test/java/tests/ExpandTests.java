package tests;

import engine.driver.DriverFactory;
import pages.expand.*;
import tests.baseTest.BaseTest;
import engine.actions.BrowserActions;
import engine.actions.DevToolsActions;
import engine.utils.Faker;
import org.testng.annotations.*;
import engine.constants.FrameworkConfigs;
import engine.utils.ExcelReader;

import java.util.LinkedHashMap;

public class ExpandTests extends BaseTest {

    HomePage homePage;
    WebInputs webInputs;
    LoginPage loginPage;
    OneTimePass oneTimePass;
    DynamicTable dynamicTable;
    DragAndDrop dragAndDrop;
    FileDownloader fileDownloader;
    MultipleTests multipleTests;
// web inputs test
    @Test(dataProvider = "webInputsData")
    public void webInputsTest(LinkedHashMap<String,String> data){
        homePage.navigateHomePage();
        homePage.clickOnSubLink(data.get("link name"));
        webInputs.typeInputDate(data.get("input date"));
        webInputs.typeInputPass(data.get("pass"));
        webInputs.typeInputNum(data.get("num input"));
        webInputs.typeInputText(data.get("text input"));
        webInputs.clickDisplayBtn();
        webInputs.clickClearBtn();
        webInputs.typeInputNum(data.get("num input"));
        webInputs.clickDisplayBtn();
        webInputs.assertText(data.get("num input"));
            }

//login and register test
        @Test(dataProvider = "loginData")
    public void loginTest(LinkedHashMap<String,String> data) {
            homePage.navigateHomePage();
            homePage.clickOnSubLink("Test Login Page");
            loginPage.loginCycle(data.get("user name"),data.get("pass"),data.get("Assertion"));
        }

        @Test
    public void registerTest(){
        String newUserName= Faker.userName;
        String pass= Faker.pass;
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Test Register Page");
        loginPage.typeUserName(newUserName);
        loginPage.typePassword(pass);
        loginPage.typeConPassword(pass);
        loginPage.clickRegister();
        loginPage.loginCycle(newUserName,pass,"pass");

            }
//forget password test doesn't send any emails

    // OTP code test
    @Test
    public void otpTest() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("OTP: One Time Password");
        String otp=oneTimePass.getOTP();
        oneTimePass.typeEmail("test@gmail.com");
        oneTimePass.clickSendOTP();
        oneTimePass.typeOTP("476798");
        oneTimePass.clickVerifyOTP();
        oneTimePass.assertFail();
        BrowserActions.navigateBack(DriverFactory.getDriver());
        BrowserActions.navigateBack(DriverFactory.getDriver());
        oneTimePass.typeEmail("practice@expandtesting.com");
        oneTimePass.clickSendOTP();
        oneTimePass.typeOTP(otp);
        oneTimePass.clickVerifyOTP();
        oneTimePass.assertWelcomeMessage();
    }


    @Test
    public void dynamicTable() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Dynamic Table");
        dynamicTable.compareValues();
    }

    @Test
    public void dynamicPaginationTable() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Dynamic Pagination Table");
        dynamicTable.compareDynamicValues();
    }

    @Test
    public void dragAndDropTest() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Drag and Drop");
        dragAndDrop.dragAToB();
    }

    @Test
    public void dragCircleTest() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Drag and Drop Circles");
        dragAndDrop.circleDragging();

    }

    @Test
    public void fileDownloader() {
        homePage.navigateHomePage();
        new DevToolsActions(DriverFactory.getDriver()).createSession().setDownloadFolder("C:\\Users\\USER\\Downloads");
        homePage.clickOnSubLink("File Downloader");
        fileDownloader.downloadFile();
    }
    @Test
    public void fileUploader() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("File Upload");
        fileDownloader.fileUpload();
    }

    @Test
    public void autoCompleteTest() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Autocomplete");
        multipleTests.autoComplete("Taiwan");
    }

    @Test
    public void alert() {
        homePage.navigateHomePage();
        multipleTests.alert();
        homePage.clickOnSubLink("Secure File Download");
    }

    @Test
    public void shadowElement() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Shadow DOM");
        multipleTests.shadowDom();
    }
    @Test
    public void scroll() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Infinite Scroll");
        multipleTests.scroll();
    }
    @Test
    public void brokenImg() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Broken Images");
        multipleTests.brokenImg();
    }

    @Test
    public void alerts() {
         homePage.navigateHomePage();
        homePage.clickOnSubLink("JavaScript Dialogs");
        multipleTests.alerts();
    }

    @Test
    public void scrollBar() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Scrollbars");
        multipleTests.scrollInner();
    }


    @DataProvider(name = "webInputsData")
    private Object[][]  webInputsData(){
return new ExcelReader().
        readRowAsLinkedHashMapThroughCondition(FrameworkConfigs.testDataPath()+"data.xlsx","Web Inputs","run","true");
    }

    @DataProvider(name = "loginData")
    private Object[][]  loginData(){
return new ExcelReader().
        readRowAsLinkedHashMapThroughCondition(FrameworkConfigs.testDataPath()+"data.xlsx","Login Page","run","true");
    }


@BeforeClass
    protected void initClasses(){
    homePage=new HomePage(DriverFactory.getDriver());
    webInputs=new WebInputs(DriverFactory.getDriver());
    loginPage=new LoginPage(DriverFactory.getDriver());
    oneTimePass=new OneTimePass(DriverFactory.getDriver());
    dynamicTable=new DynamicTable(DriverFactory.getDriver());
    dragAndDrop=new DragAndDrop(DriverFactory.getDriver());
    fileDownloader=new FileDownloader(DriverFactory.getDriver());
    multipleTests=new MultipleTests(DriverFactory.getDriver());
}

}
