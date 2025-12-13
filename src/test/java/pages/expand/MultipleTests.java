package pages.expand;

import engine.actions.*;
import engine.reporters.Loggers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MultipleTests extends HomePage {

    public MultipleTests(WebDriver driver) {
        super(driver);
    }

    public void autoComplete(String country){
        By countrySelection= By.id("country");
        By assertion= By.id("result");
        By submitButton=By.cssSelector("button[class='btn btn-primary mx-2']");
        By selection=By.xpath("//input[@value='"+country+"']//parent::div");
        ElementActions.typeInElement(driver,countrySelection,"ta");
        Waits.waitToBeVisible(driver,selection);
        ElementActions.clickElement(driver,selection);
        ElementActions.clickElement(driver,submitButton);
        Waits.waitElementToContainText(driver,assertion,country);
        Assert.assertTrue(ElementActions.getText(driver,assertion).contains(country));
    }

    public void alert(){
        new DevToolsActions(driver).createSession().handleBasicAuth("admin","admin");
    }

    public void shadowDom(){
        WebElement e=JSActions.getShadowElement(driver,By.id("shadow-host"),"button[id='my-btn']");
        System.out.println(e.getText());
    }

    public void alerts(){
        By jsAlert=By.id("js-alert");
        By jsConfirm=By.id("js-confirm");
        By jsPrompt=By.id("js-prompt");
        By dialog=By.id("dialog-response");
        ElementActions.clickElement(driver,jsAlert);
        driver.switchTo().alert().dismiss();
        Assert.assertTrue(ElementActions.getText(driver,dialog).equalsIgnoreCase("ok"));
        ElementActions.clickElement(driver,jsConfirm);
        driver.switchTo().alert().accept();
        Assert.assertTrue(ElementActions.getText(driver,dialog).equalsIgnoreCase("ok"));
        ElementActions.clickElement(driver,jsPrompt);
        driver.switchTo().alert().sendKeys("test");
        driver.switchTo().alert().accept();
        Assert.assertTrue(ElementActions.getText(driver,dialog).equalsIgnoreCase("test"));

    }

    public void brokenImg() {
        List<WebElement> images = driver.findElements(By.tagName("img"));
        for (WebElement img : images) {
            String imageUrl = img.getAttribute("src");
            try {
                HTTPActions httpActions=new HTTPActions(imageUrl);
                httpActions.setMethod("GET").startConnection();
                int responseCode = httpActions.getResponseCode();
                if (responseCode != 200) {
                    System.out.println("Broken image: " + imageUrl + " - Status: " + responseCode);
                }
            } catch (Exception e) {
            }
        }
    }

        public void scroll () {
        int num=0;
            while (num<=5) {
                Number heightObj = (Number) JSActions.executeScript(driver,"return document.body.scrollHeight");
                long lastHeight = heightObj != null ? heightObj.longValue() : 0;
                AtomicLong newHeight = new AtomicLong();
                // Scroll to bottom
                JSActions.executeScript(driver,"window.scrollTo(0, document.body.scrollHeight);");
                try {
                    Waits.explicitWaitShortTime(driver).until(x -> {
                        Number newHeightObj = (Number)
                                JSActions.executeScript(driver,"return document.body.scrollHeight");
                        newHeight.set(newHeightObj != null ? newHeightObj.longValue() : 0);
                        return lastHeight != newHeight.get();
                    });
                } catch (Exception e) {
                 Loggers.log.info("Reached the end of the page");
                }
                // Break if no new content is loaded
                if (newHeight.get() == lastHeight) {
                    break;
                }
                num++;
            }
        }

        public void scrollInner () {
        By scrollingElement=By.xpath("//table//parent::div//parent::div");
        int num=0;
            while (num<5) {
                Number heightObj = (Number) JSActions.executeScript(driver,"return arguments[0].scrollHeight",driver.findElement(scrollingElement));
                long lastHeight = heightObj != null ? heightObj.longValue() : 0;
                AtomicLong newHeight = new AtomicLong();
                // Scroll to bottom
                JSActions.executeScript(driver,"arguments[0].scrollTo(0, arguments[0].scrollHeight);",driver.findElement(scrollingElement));
                try {
                    Waits.explicitWaitShortTime(driver).until(x -> {
                        Number newHeightObj = (Number)
                                JSActions.executeScript(driver,"return arguments[0].scrollHeight",driver.findElement(scrollingElement));
                        newHeight.set(newHeightObj != null ? newHeightObj.longValue() : 0);
                        return lastHeight != newHeight.get();
                    });
                } catch (Exception e) {
                 Loggers.log.warn("Reached the end of the page");
                }
                // Break if no new content is loaded
                if (newHeight.get() == lastHeight) {
                    break;
                }
                num++;
            }
        }
    }

