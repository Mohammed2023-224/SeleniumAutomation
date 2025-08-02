
import base.BaseTest;
import engine.actions.BrowserActions;
import engine.actions.DevToolsActions;
import engine.utils.Faker;
import pages.expand.MultipleTests;
import engine.constants.Constants;
import engine.utils.ExcelReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.expand.*;

import java.util.LinkedHashMap;

public class ExpandTests extends BaseTest {

    HomePage homePage;
    WebInputs webInputs;
    LoginPage loginPage;
    OneTimePass    oneTimePass;
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
    public void loginTest(LinkedHashMap<String,String> data){
        homePage.navigateHomePage();
        homePage.clickOnSubLink(data.get("link name"));
        loginPage.typeUserName(data.get("su user name"));
        loginPage.typePassword(data.get("su pass"));
        loginPage.clickLogin();
        loginPage.assertSuccessLogin(data.get("su user name"));
        loginPage.clickLogOut();
        loginPage.typeUserName("tes");
        loginPage.typePassword("tes");
        loginPage.clickLogin();
        loginPage.assertFailLogin();
        String newUserName= Faker.userName;
        String pass= Faker.pass;
        loginPage.clickRegisterLink();
        loginPage.typeUserName(newUserName);
        loginPage.typePassword(pass);
        loginPage.typeConPassword(pass);
        loginPage.clickRegister();
        loginPage.typeUserName(newUserName);
        loginPage.typePassword(pass);
        loginPage.clickLogin();
        loginPage.assertSuccessLogin(newUserName);
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
        BrowserActions.navigateBack(driver);
        BrowserActions.navigateBack(driver);
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
        new DevToolsActions(driver).createSession().setDownloadFolder("C:\\Users\\USER\\Downloads");
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
        readRowAsLinkedHashMapThroughCondition(Constants.testDataPath+"w.xlsx","Web Inputs","run","true");
    }

    @DataProvider(name = "loginData")
    private Object[][]  loginData(){
return new ExcelReader().
        readRowAsLinkedHashMapThroughCondition(Constants.testDataPath+"w.xlsx","Login Page","run","true");
    }


@BeforeMethod
    private void initClasses(){
    homePage=new HomePage(driver);
    webInputs=new WebInputs(driver);
    loginPage=new LoginPage(driver);
    oneTimePass=new OneTimePass(driver);
    dynamicTable=new DynamicTable(driver);
    dragAndDrop=new DragAndDrop(driver);
    fileDownloader=new FileDownloader(driver);
    multipleTests=new MultipleTests(driver);
}

}
