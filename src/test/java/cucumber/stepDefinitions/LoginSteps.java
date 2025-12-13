package cucumber.stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.expand.HomePage;
import pages.expand.LoginPage;

public class LoginSteps {

    HomePage homePage;
    LoginPage loginPage;


    @Before
    public void InitDriver() {
         homePage=new HomePage(DriverFactory.getDriver());
        loginPage=new LoginPage(DriverFactory.getDriver());


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
            loginPage.assertLoginStatus("fail");
        }
        else {
            loginPage.assertLoginStatus("pass");
        }
    }
}
