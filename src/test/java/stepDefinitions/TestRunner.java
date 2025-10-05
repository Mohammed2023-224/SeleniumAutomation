package stepDefinitions;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/feature",glue = {"stepDefinitions"}, plugin = {"pretty"})
public class TestRunner extends AbstractTestNGCucumberTests {
}
