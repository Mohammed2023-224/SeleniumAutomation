package base;

import engine.constants.Constants;
import engine.driver.SetupDriver;
import engine.listeners.TestNg;
import io.cucumber.java.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Listeners(TestNg.class)
public class BaseTest {
    public WebDriver driver;

    @Parameters("browser")
    @BeforeClass
    public void initDriver(ITestContext con,  @Optional String browser) {
        browser=browser==null||browser.isEmpty()? Constants.browser:browser;

        if(Constants.executionType.equalsIgnoreCase("local")) {
            driver = new SetupDriver().startDriver(browser);
        } else if (Constants.executionType.equalsIgnoreCase("remote")) {
            driver = new SetupDriver().startDriverRemotely(browser);
        }
        con.setAttribute("driver",driver);
    }

//    @AfterClass
//    public void stopDriver() {
//        this.driver.quit();
//    }




}


