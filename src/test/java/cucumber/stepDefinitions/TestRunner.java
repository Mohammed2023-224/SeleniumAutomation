package cucumber.stepDefinitions;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/feature",glue = {"cucumber/stepDefinitions"}, plugin = {"pretty"})
public class TestRunner extends AbstractTestNGCucumberTests {
}
