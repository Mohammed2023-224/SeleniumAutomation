package stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.expand.*;

public class LoginSteps {

    HomePage homePage;
//    HomePage homePage=new HomePage(driver);
//    WebInputs webInputs=new WebInputs(driver);
//    LoginPage loginPage=new LoginPage(driver);
//    OneTimePass oneTimePass=new OneTimePass(driver);
//    DynamicTable dynamicTable=new DynamicTable(driver);
//    DragAndDrop dragAndDrop=new DragAndDrop(driver);
//    FileDownloader fileDownloader=new FileDownloader(driver);
//    MultipleTests multipleTests=new MultipleTests(driver);;

    @Before
    public void InitDriver() {
         homePage=new HomePage(DriverFactory.getDriver());
        WebInputs webInputs=new WebInputs(DriverFactory.getDriver());
        LoginPage loginPage=new LoginPage(DriverFactory.getDriver());
        OneTimePass oneTimePass=new OneTimePass(DriverFactory.getDriver());
        DynamicTable dynamicTable=new DynamicTable(DriverFactory.getDriver());
        DragAndDrop dragAndDrop=new DragAndDrop(DriverFactory.getDriver());
        FileDownloader fileDownloader=new FileDownloader(DriverFactory.getDriver());
        MultipleTests multipleTests=new MultipleTests(DriverFactory.getDriver());;

    }

    @Given("user is on login page")
    public void user_is_on_login_page() {
        homePage.navigateHomePage();
    }
    @When("user enters {string} and {string}")
    public void user_enters_user1_and_pass1(String username, String password) {
        System.out.println("data");

    }
    @When("user clicks on loging page")
    public void user_clicks_on_loging_page() {
        System.out.println("login");

    }
    @Then("Error appears that credentials is wrong")
    public void error_appears_that_credentials_is_wrong() {
        System.out.println("invalid");

    }
}
