package base;

import engine.constants.Constants;
import engine.driver.SetupDriver;
import engine.listeners.TestNg;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(TestNg.class)
public class BaseTest {
    public WebDriver driver;

    @BeforeClass
    public void initDriver(ITestContext con) {
        if(Constants.executionType.equalsIgnoreCase("local")) {
            driver = new SetupDriver().startDriver(Constants.browser);
        } else if (Constants.executionType.equalsIgnoreCase("remote")) {
            driver = new SetupDriver().startDriverRemotely(Constants.browser);
        }
        con.setAttribute("driver",driver);
    }

//    @AfterClass
//    public void stopDriver() {
//        this.driver.quit();
//    }




}


