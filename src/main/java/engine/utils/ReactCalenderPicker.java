package engine.utils;

import engine.actions.ElementActions;
import engine.actions.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ReactCalenderPicker {
    private static By monthAndYear=By.xpath("//div[contains(@class,'MuiPickersCalendarHeader')and contains(@id,'grid')]");
    private static By previousMonthArrow=By.xpath("//button[contains(@title,'Previous mon')]");
    private static By nextMonthArrow=By.xpath("//button[contains(@title,'Next mon')]");
    private static By yearSelectionViewArrow=By.xpath("//button[contains(@aria-label,'calendar view is open, switch to year view')]");

    private static By yearSelectionView=By.xpath("//div[contains(@class,'MuiYearCalendar-')]");
    private static By getDayLocator(String day){
        return By.xpath("//div[contains(@class,'MuiDayCalendar-slide')]//button[text()='"+day+"']");
    }
    private static By getYearLocator(String year){
        return By.xpath("//div[contains(@class,'MuiYearCalendar-')]//button[text()='"+year+"']");
    }
    private static String[]  monthsArray={"January","February","March","April","May","June","July",
            "August","September","October","November","December"};


    private static int getMonthIndexIgnoreCase(String month) {
        for (int i = 0; i < monthsArray.length; i++) {
            if (monthsArray[i].equalsIgnoreCase(month)) {
                return i;
            }
        }
        return -1;
    }

    public static void calenderPicker(WebDriver driver,String year, String month, String day){
        //select year
        int retryYear=2;
        int yearIndex=0;
        String currentMonthAndYear= ElementActions.getText(driver,monthAndYear);
        while (yearIndex<retryYear) {
            if (!currentMonthAndYear.contains(year)){
                System.out.println(0);
                ElementActions.clickElement(driver, yearSelectionViewArrow);
                Waits.waitToBeVisible(driver, yearSelectionView);
                ElementActions.clickElement(driver, getYearLocator(year));
                Waits.waitToBeInvisible(driver, yearSelectionView);
                currentMonthAndYear= ElementActions.getText(driver,monthAndYear);
                yearIndex++;
            }
            if(currentMonthAndYear.contains(year)){
                break;
            }
        }
//select month
        int retryMonth=2;
        int monthIndex=0;
        String currentMonth=currentMonthAndYear.split(" ")[0];
        while (monthIndex<retryMonth) {
            int monthsDifferences = 0;
            if (!currentMonth.contains(month)) {
                monthsDifferences = getMonthIndexIgnoreCase(month) - getMonthIndexIgnoreCase(currentMonth);
                if (monthsDifferences > 0) {
                    for (int c = 0; c < monthsDifferences; c++) {
                        ElementActions.clickElement(driver, nextMonthArrow);
                    }
                } else if (monthsDifferences < 0) {
                    for (int c = 0; c > monthsDifferences; c--) {
                        ElementActions.clickElement(driver, previousMonthArrow);
                    }
                }
                currentMonthAndYear= ElementActions.getText(driver,monthAndYear);
                if(!currentMonthAndYear.split(" ")[0].equalsIgnoreCase(month)){
                    monthIndex++;
                }
                else {
                    break;
                }
            }
            if(currentMonthAndYear.contains(month)){
                break;
            }
            monthIndex++;
        }

        // select Day
        ElementActions.clickElement(driver,getDayLocator(day));
        Waits.fluentWaitShortTime(driver).until(ExpectedConditions.
                attributeToBe(getDayLocator(day),"aria-selected","true"));

    }
}