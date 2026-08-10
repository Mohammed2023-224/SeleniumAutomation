package pages.appiumDemo;

import engine.actions.ElementActions;
import engine.actions.Waits;
import engine.assertions.HardAssertions;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class TimeScreen {
    private AndroidDriver  driver;
    public TimeScreen(AndroidDriver driver){
        this.driver=driver;
    }

    private By hours= By.id("android:id/hours");
    private By minutes= By.id("android:id/minutes");
    private By am= By.id("android:id/am_label");
    private By pm= By.id("android:id/pm_label");
    By screenTitle=By.xpath("//android.widget.TextView[@text=\"Time Activity\"]");

    private By clockTimeLocator(String time){
       return By.xpath("//android.widget.RadialTimePickerView.RadialPickerTouchHelper[@content-desc="+time+"]");
    }


    private void waitTitleScreen(){
        Waits.waitToBeVisible(screenTitle);
    }

    public void selectHours(){
        waitTitleScreen();
        ElementActions.clickElement(driver,hours);
    }

    public void selectMinutes(){
        waitTitleScreen();
        ElementActions.clickElement(driver,minutes);
    }

    public void selectTime(String time){
        Waits.waitToBeClickable(clockTimeLocator(time));
        ElementActions.clickElement(driver,clockTimeLocator(time));
    }

    public void assertTime(String hour,String minute){
        HardAssertions.assertTru(()->ElementActions.getText(driver,hours).equalsIgnoreCase(hour),"Assert correct hours");
        HardAssertions.assertTru(()->ElementActions.getText(driver,minutes).equalsIgnoreCase(minute),"Assert correct minutes");
    }
}
