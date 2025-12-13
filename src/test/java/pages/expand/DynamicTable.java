package pages.expand;

import engine.actions.ElementActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class DynamicTable extends HomePage {
    private By yellowChrome = By.id("chrome-cpu");

    private By tableData(String data, String columnNum) {
        return By.xpath("//td[text()='" + data + "']//ancestor::tr//td[" + columnNum + "]");
    }

    public DynamicTable(WebDriver driver) {
        super(driver);
    }

    public void compareValues() {

        String chrome = ElementActions.getText(driver, yellowChrome);
        List<WebElement> ll = driver.findElements(By.xpath("//thead//tr//th"));
        String index = "";
        for (int i = 1; i <= ll.size(); i++) {
            if (ElementActions.getText(driver, By.xpath("//thead//tr//th[" + i + "]"))
                    .equalsIgnoreCase("CPU")) {
                index = String.valueOf(i);
                break;
            }
        }
        String cpuValue = ElementActions.getText(driver, tableData("Chrome", index));

        String num1Str = cpuValue.replaceAll("[^0-9.]", "").trim();
        String num2Str = chrome.replaceAll("[^0-9.]", ""); // removes non-numeric (except dot)
// Parse to double
        double num1 = Double.parseDouble(num1Str);
        double num2 = Double.parseDouble(num2Str);

// Compare
        if (num1 == num2) {
            System.out.println("Values match!");
        } else {
            System.out.println("Values are different.");
            System.out.println(Double.valueOf(num1Str).compareTo(Double.valueOf(num2Str)));
        }
    }

    public void compareDynamicValues() {
        ElementActions.clickElement(driver,By.name("example_length"));
        ElementActions.selectOption(driver,By.name("example_length"),"10");
        Assert.assertTrue(ElementActions.getText(driver,By.id("example_info")).
                equalsIgnoreCase("Showing 1 to 10 of 10 entries"));
        List<WebElement> ll = driver.findElements(By.xpath("//thead//tr//th"));
        String index = "";
        for (int i = 1; i <= ll.size(); i++) {
            if (ElementActions.getText(driver, By.xpath("//thead//tr//th[" + i + "]"))
                    .equalsIgnoreCase("Major")) {
                index = String.valueOf(i);
                break;
            }
        }
        String cpuValue = ElementActions.getText(driver, tableData("Daniel Martinez", index));
        System.out.println(cpuValue);
    }
}
