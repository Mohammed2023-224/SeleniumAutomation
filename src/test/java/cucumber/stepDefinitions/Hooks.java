package cucumber.stepDefinitions;

import engine.actions.SystemMethods;
import engine.constants.FrameworkConfigs;
import engine.driver.SetupDriver;
import engine.enums.Browsers;
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
        String browser = !FrameworkConfigs.browser().isEmpty() ? FrameworkConfigs.browser() : "edge";
        String port=System.getProperty("port");
        port = FrameworkConfigs.localExecution()?"":port == null || port.isEmpty()? FrameworkConfigs.proxy():port;
        driver = new SetupDriver().startDriver(Browsers.valueOf(browser.toUpperCase()),FrameworkConfigs.localExecution(),port);
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
                FrameworkConfigs.reportsPath() + System.getProperty("testLogFileName") + ".log");
        DriverFactory.getDriver().quit();
        DriverFactory.unload();
    }

    @AfterAll
    public static void generateAllure(){
        if(FrameworkConfigs.sendReportEmail()){
            SystemMethods.runFile(FrameworkConfigs.allureGenerationPath());
            GmailHandler gmailHandler=new GmailHandler("test");
            gmailHandler.sendEmail(FrameworkConfigs.emailTo(), FrameworkConfigs.emailCc(), FrameworkConfigs.emailSubject()
                    , FrameworkConfigs.emailBody(), FrameworkConfigs.emailAttachmentPath());
        }
        if (FrameworkConfigs.openAllure()) {
            Loggers.log.info("start allure report pls don't stop the execution");
            SystemMethods.runFile(FrameworkConfigs.allureBat());
        }
    }
}
