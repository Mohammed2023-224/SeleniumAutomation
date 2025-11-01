package cucumber.stepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.expand.DragAndDrop;
import pages.expand.HomePage;
import pages.expand.LoginPage;

public class MouseActionsSteps {

    HomePage homePage;
    DragAndDrop dragAndDrop;

    @Before
    public void InitDriver() {
        homePage=new HomePage(DriverFactory.getDriver());
        dragAndDrop=new DragAndDrop(DriverFactory.getDriver());


    }

    @Given("user on drag and drop page")
    public void navigateToCorrectLink() {
        homePage.navigateHomePage();
        homePage.clickOnSubLink("Drag and Drop");
    }
    @When("user drags A to B")
    public void dragCorrectElement() {
        dragAndDrop.dragAToB();

    }
}
