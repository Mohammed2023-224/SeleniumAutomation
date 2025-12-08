package engine.constants;

import engine.utils.JSONReader;
import engine.utils.PropertyReader;

import java.util.Map;

public class Constants {
     static {
          PropertyReader.readAllProperties();
     }
    private Constants() {}
     public static final  int shortWaitTime= Integer.parseInt(PropertyReader.readProp("shortWaitTime"));
     public static final int longWaitTime=Integer.parseInt(PropertyReader.readProp("longWaitTime"));
     public static final String openAllure=PropertyReader.readProp("openAllureAfterTest");
     public static final String allureFile=PropertyReader.readProp("allure_bat_file_path");
     public static  final String generateAllureReport=PropertyReader.readProp("allure_generation_path");
     public static final String compressReport=PropertyReader.readProp("allure_compression_path");
     public static final String reportsPath=PropertyReader.readProp("report_logs_path");
     public static final int retryCount=Integer.parseInt(PropertyReader.readProp("RetryCount"));
     public static  final String testDataPath=PropertyReader.readProp("test_data_path");
     public static final String url=PropertyReader.readProp("mainurl");
     public static final String headlessMode=PropertyReader.readProp("headless");
     public static final String maximizedMode=PropertyReader.readProp("maximized");
     public static final String popupBlocker=PropertyReader.readProp("popup_blocker");
     public static final String browser=PropertyReader.readProp("Browser");
     public static final String proxyURL=PropertyReader.readProp("proxy");
     public static final String executionType=PropertyReader.readProp("execution");
     public static final String gmailToken=PropertyReader.readProp("gmail_token_path");
     public static final String gmailCredential=PropertyReader.readProp("gmail_credentials");
     public static final String testPlayGroundMainPage=PropertyReader.readProp("testAutomationPlayGroundLink");
     public static final String generateAndSendReport=PropertyReader.readProp("generateAndSendReport");
     public static final String emailRecipient=PropertyReader.readProp("Email_to");
     public static final String emailCopied=PropertyReader.readProp("Email_CC");
     public static final String emailSubject=PropertyReader.readProp("Email_subject");
     public static final String emailAttachmentPath=PropertyReader.readProp("Email_attachment_path");
     public static final String emailBody=PropertyReader.readProp("Email_body");
    public static final String downloadsPath= PropertyReader.readProp("downloaded_files_Path");


}
