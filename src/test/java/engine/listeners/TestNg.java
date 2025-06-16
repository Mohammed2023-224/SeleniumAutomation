package engine.listeners;

import java.util.ArrayList;

import engine.actions.SystemMethods;
import engine.reporters.Loggers;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestNg extends Allure implements ITestListener {
    int numberOfFailedTests=0;
    int numberOfSuccessTest=0;
    int numberOfSkippedTests=0;
    ArrayList<String> successfulTests= new ArrayList<>();
    ArrayList<String> failedTests= new ArrayList<>();
    ArrayList<String> skippedTests= new ArrayList<>();

    public void onTestStart(ITestResult result){
        SystemMethods.deleteDirectory("test_output/output/logs/tests");
        SystemMethods.deleteDirectory("allure-results");
        Loggers.log.info("Start test : {}",result.getName());
    }
}
