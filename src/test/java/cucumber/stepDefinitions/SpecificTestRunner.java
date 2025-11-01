package cucumber.stepDefinitions;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/feature",glue = {"cucumber/stepDefinitions"},tags = "@smokeTest",
        plugin = {"pretty","io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "html:target/cucumber-report.html"})
public class SpecificTestRunner extends AbstractTestNGCucumberTests {
}
