package cucumber.stepDefinitions;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/feature",glue = {"cucumber/stepDefinitions"},
        plugin = {"pretty","io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"})
public class TestRunner extends AbstractTestNGCucumberTests {
}
