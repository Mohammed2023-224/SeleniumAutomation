
import engine.driver.SetupDriver;
import engine.listeners.TestNg;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners(TestNg.class)
public class BaseTest {
    protected WebDriver driver;

    @BeforeClass
    public void initDriver(ITestContext con) {
        driver = new SetupDriver().startDriver("edge");
        con.setAttribute("driver",driver);

    }
//    @AfterClass
//    public void stopDriver() {
//        this.driver.quit();
//    }




}


