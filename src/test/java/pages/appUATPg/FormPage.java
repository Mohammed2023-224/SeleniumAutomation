package pages.appUATPg;


import engine.actions.*;
import engine.reporters.Loggers;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FormPage {
private final WebDriver driver;
public FormPage(WebDriver driver){
    this.driver=driver;
}
String path="C:\\Users\\USER\\Downloads\\";
String firstFile="Nada_Ali_Resume_updated-4.pdf";
String secondFile="pom.txt";
String thirdFile="sample_text.txt";
    By expField= By.id("exp");
    By expFieldAssertion= By.id("exp_help");
    By checkCheckBoxes= By.id("check_validate");
    By redValidate= By.id("rad_validate");
    By selectTool= By.id("select_tool");
    By selectToolValidate= By.id("select_tool_validate");
    By multipleSelection= By.id("select_lang");
    By multipleSelectionValidate= By.id("select_lang_validate");
    By notesField= By.id("notes");
    By notesFieldValidate= By.id("area_notes_validate");
    By onlyReadField= By.id("common_sense");
    By readGerman= By.id("german");
    By readGermanValidate= By.id("german_validate");
    By fluency= By.id("fluency");
    By fluencyValidate= By.id("fluency_validate");
    By uploadCV= By.id("upload_cv");
    By uploadCVValidate= By.id("validate_cv");
    By uploadFiles= By.id("upload_files");
    By uploadFilesValidate= By.id("validate_files");
    By downloadFile= By.id("download_file");
    By salary= By.id("salary");
    By city= By.id("validationCustom03");
    By state= By.id("validationCustom04");
    By zip= By.id("validationCustom05");
    By invalidZip= By.id("invalid_zip");
    By agree= By.id("invalidCheck");
    By invalidTerms= By.id("invalid_terms");
    By nonEnglishText= By.id("नाव");
    By nonEnglishTextValidate= By.id("नाव_तपासा");
    By nonEnglishSelectionValidate= By.id("check_validate_non_english");
    By submitButton= By.tagName("button");

private By options(String option){
    return By.xpath("//input[@value='"+option+"']");
}

private void typeField(String text,By element){
    ElementActions.typeInElement(driver,element,text);
}

private void clickOption(String text){
    ElementActions.clickElement(driver,options(text));
}

 private void selectOptionFromDDL(String text,By element){
    ElementActions.selectDDLOptionText(driver,element,text);
}

private void assertText(By element,String text){
    Loggers.log.info(ElementActions.getText(driver,element));
    Loggers.log.info(text);
    Assert.assertTrue(ElementActions.getText(driver,element).contains(text));
    Loggers.log.info("Assertion successful where "+ElementActions.getText(driver,element)+" contains "+ text);
}

public void userFillsExperience(String text){
    typeField(text,expField);
    assertText(expFieldAssertion,text);
    Allure.step("User fills in experience field");
}
public void userFillsProgrammingLanguagesCheckboxes() {
    Set<String> expectedSelections = new LinkedHashSet<>();
    toggleAndAssert("PYTHON", expectedSelections);
    toggleAndAssert("JAVASCRIPT", expectedSelections);
    toggleAndAssert("PYTHON", expectedSelections);
    Allure.step("User fills in programming languages checkboxes");
}
public void userFillsToolsRadioButtons(){
    ArrayList<String> data = new ArrayList<>(List.of("SELENIUM", "PROTRACTOR"));
    for (String i : data){
        clickOption(i);
        assertText(redValidate,i);
    }
    Allure.step("User fills in tools radio buttons");
}
// Can be handled better through tests dividing and external data calling and method dividing per purpose
public void handleForm(){
    selectOptionFromDDL("Cypress",selectTool);
    assertText(selectToolValidate,"cyp");
    selectOptionFromDDL("Python",multipleSelection);
    assertText(multipleSelectionValidate,"python");
    selectOptionFromDDL("Java",multipleSelection);
    assertText(multipleSelectionValidate,"java");

    ElementActions.pressKeyboardKeys(driver,multipleSelection, Keys.CONTROL);
    selectOptionFromDDL("TypeScript",multipleSelection);
    assertText(multipleSelectionValidate,"java,python,typescript");
    typeField("test",notesField);
    assertText(notesFieldValidate,"test");
    Assert.assertTrue(driver.findElement(onlyReadField).getDomProperty("placeholder").contains("Common Sense"));
    JSActions.clickUsingJavaScript(driver,readGerman);
    assertText(readGermanValidate,"true");
    JSActions.clickUsingJavaScript(driver,readGerman);
    assertText(readGermanValidate,"false");
    DragAndDropActions.dragAndDropByLocation(driver,fluency,-100,0);
//    Assert.assertTrue(ElementActions.getText(driver,fluencyValidate).contains("0"));
    typeField(path+firstFile,uploadCV);
    assertText(uploadCVValidate,firstFile);
    typeField(path+secondFile+" \n "+path+firstFile,uploadFiles);
    assertText(uploadFilesValidate,secondFile+" "+firstFile);

    Assert.assertTrue(driver.findElement(salary).getDomProperty("placeholder").contains("You should not provide this"));
    ElementActions.clickElement(driver,downloadFile);
Waits.waitForFileToBeDownloaded(driver,path+thirdFile);
    Assert.assertTrue(SystemMethods.checkExistenceOfFile(path+thirdFile));
    Assert.assertTrue(SystemMethods.readFileContent(path+thirdFile).contains("File downloaded by AutomationCamp"));
    ElementActions.typeInElement(driver,city,"test");
    ElementActions.typeInElement(driver,state,"test");
    ElementActions.clickElement(driver,submitButton);
    assertText(invalidZip,"Please provide a valid zip.");
    assertText(invalidTerms,"You must agree before submitting.");
    typeField("test",zip);
    ElementActions.clickElement(driver,agree);
    ElementActions.clickElement(driver,submitButton);
    assertText(city,"");
    typeField("test",nonEnglishText);
    assertText(nonEnglishTextValidate,"test");
    clickOption("मराठी");
    assertText(nonEnglishSelectionValidate,"मराठी");
    clickOption("ગુજરાતી");
    assertText(nonEnglishSelectionValidate,"ગુજરાતી");
}


    private void toggleAndAssert(String language, Set<String> expected) {
        clickOption(language);
        if (expected.contains(language)) {
            expected.remove(language);
        } else {
            expected.add(language);
        }
        assertText(checkCheckBoxes, String.join(" ", expected));
    }
}
