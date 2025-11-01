package cucumber.stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.expand.*;

public class LoginSteps {

    HomePage homePage;
//    WebInputs webInputs=new WebInputs(driver);
    LoginPage loginPage;
//    OneTimePass oneTimePass=new OneTimePass(driver);
//    DynamicTable dynamicTable=new DynamicTable(driver);
//    DragAndDrop dragAndDrop=new DragAndDrop(driver);
//    FileDownloader fileDownloader=new FileDownloader(driver);
//    MultipleTests multipleTests=new MultipleTests(driver);;

    @Before
    public void InitDriver() {
         homePage=new HomePage(DriverFactory.getDriver());
//        WebInputs webInputs=new WebInputs(DriverFactory.getDriver());
        loginPage=new LoginPage(DriverFactory.getDriver());
//        LoginPage loginPage=new LoginPage(DriverFactory.getDriver());
//        OneTimePass oneTimePass=new OneTimePass(DriverFactory.getDriver());
//        DynamicTable dynamicTable=new DynamicTable(DriverFactory.getDriver());
//        DragAndDrop dragAndDrop=new DragAndDrop(DriverFactory.getDriver());
//        FileDownloader fileDownloader=new FileDownloader(DriverFactory.getDriver());
//        MultipleTests multipleTests=new MultipleTests(DriverFactory.getDriver());;

    }

    @Given("user is on login page")
    public void user_is_on_login_page() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Test Login Page");
    }
    @When("user enters {string} and {string}")
    public void user_enters_user1_and_pass1(String username, String password) {
        loginPage.typeUserName(username);
        loginPage.typePassword(password);

    }
    @When("user clicks on loging button")
    public void user_clicks_on_loging_button() {
        loginPage.clickLogin();

    }
    @Then("Assert {string} login")
    public void assertLogin(String assertion) {
        if( assertion.equalsIgnoreCase("fail")){
            loginPage.assertFailLogin();
        }
        else {
            loginPage.assertSuccessLogin();
        }

    }
}
