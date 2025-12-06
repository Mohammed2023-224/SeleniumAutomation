package cucumber.stepDefinitions;

import engine.actions.SystemMethods;
import engine.constants.Constants;
import engine.driver.SetupDriver;
import engine.listeners.AllureListener;
import engine.listeners.ListenerHelper;
import engine.reporters.Loggers;
import engine.utils.GmailHandler;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Hooks {

    public WebDriver driver;


    @Before
    public void InitDriver(Scenario scenario) {
        String browser = !Constants.browser.isEmpty() ? Constants.browser : "edge";
        if (Constants.executionType.equalsIgnoreCase("local")) {
            driver = new SetupDriver().startDriver(browser);
        } else if (Constants.executionType.equalsIgnoreCase("remote")) {
            driver = new SetupDriver().startDriverRemotely(browser);
        }
        DriverFactory.setDriver(driver);

        String name = scenario.getName();
        WebDriver driver = DriverFactory.getDriver();
        String browserName = getBrowserName(driver);
        System.out.println("test case name: "+name);
        System.out.println("browser" +browserName);
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));

        String fileName = name + "-" + browserName + "-" + timestamp;
        fileName = fileName.replaceAll("[^a-zA-Z0-9\\-_]", "_");

        System.setProperty("testLogFileName", fileName);
        ListenerHelper.reconfigureLogs();

        Loggers.log.info("Start test: {}", name);
    }
    private String getBrowserName(WebDriver driver) {
        // Implement logic to get browser name from driver capabilities
        return driver != null ? driver.toString() : "unknown";
    }
    @After
    public void tearDriver() {
        AllureListener.saveTextLog(System.getProperty("testLogFileName") + ".log",
                Constants.reportsPath + System.getProperty("testLogFileName") + ".log");
        DriverFactory.getDriver().quit();
        DriverFactory.unload();
    }

    @AfterAll
    public static void generateAllure(){
        if(Constants.generateAndSendReport.equalsIgnoreCase("true")){
            SystemMethods.runFile(Constants.generateAllureReport);
            GmailHandler gmailHandler=new GmailHandler("test");
            gmailHandler.sendEmail(Constants.emailRecipient,Constants.emailCopied,Constants.emailSubject
                    ,Constants.emailBody,Constants.emailAttachmentPath);
        }
        if (Constants.openAllure.equalsIgnoreCase("true")) {
            Loggers.log.info("start allure report pls don't stop the execution");
            SystemMethods.runFile(Constants.allureFile);
        }
    }
}
