package cucumber.stepDefinitions;

import engine.listeners.ListenerHelper;
import engine.reporters.Loggers;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.openqa.selenium.WebDriver;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class CustomCucumberListener implements ConcurrentEventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, onTestCaseStarted);

    }
    private final Set<String> startedFeatures = new HashSet<>();

    private final EventHandler<TestCaseStarted> onTestCaseStarted = event -> {
        TestCase testCase = event.getTestCase();
        String name = testCase.getName();
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
    };

    private String getBrowserName(WebDriver driver) {
        // Implement logic to get browser name from driver capabilities
        return driver != null ? driver.toString() : "unknown";
    }


}

