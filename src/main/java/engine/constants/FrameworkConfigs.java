package engine.constants;

import engine.utils.PropertyReader;
import engine.utils.PropertyReader.*;


public class FrameworkConfigs {


    /* ======================
       Core resolver
       ====================== */

  
    /* ======================
       Configs
       ====================== */

        public static int shortWait() {
            return PropertyReader.get("shortWaitTime", Integer.class);
        }

        public static int longWait() {
            return PropertyReader.get("longWaitTime", Integer.class);
        }

        public static int retryCount() {
            return PropertyReader.get("RetryCount", Integer.class);
        }

        public static String browser() {
            return PropertyReader.get("Browser", String.class);
        }

        public static boolean headless() {
            return PropertyReader.get("headless", Boolean.class);
        }
        public static boolean popupBlocker() {
            return PropertyReader.get("headless", Boolean.class);
        }
    public static boolean openAllure() {
        return PropertyReader.get("openAllureAfterTest", Boolean.class);
    }

    public static boolean sendReportEmail() {
        return PropertyReader.get("SendReport", Boolean.class);
    }

    public static boolean generateReport() {
        return PropertyReader.get("generateReport", Boolean.class);
    }


    public static boolean maximized() {
            return PropertyReader.get("maximized", Boolean.class);
        }

        public static boolean localExecution() {
            return PropertyReader.get("local_execution", Boolean.class);
        }
        public static String testDataPath() {
            return PropertyReader.get("test_data_path", String.class);
        }

        public static boolean gridEnabled() {
            return PropertyReader.get("localSeleniumGrid", Boolean.class);
        }

        public static boolean localPathDriver() {
            return PropertyReader.get("localDriver", Boolean.class);
        }
        public static String edgeLocalDriverPath() {
            return PropertyReader.get("edge_driver_path", String.class);
        }
        public static String chromeLocalDriverPath() {
            return PropertyReader.get("chrome_driver_path", String.class);
        }

        public static String proxy() {
            return PropertyReader.get("proxy", String.class);
        }

        public static String downloadsPath() {
            return PropertyReader.get("downloaded_files_Path", String.class);
        }
        public static String xmlFilesPath() {
            return PropertyReader.get("xml_files_path", String.class);
        }
        public static boolean per_test_log() {
            return PropertyReader.get("per_test_log", Boolean.class);
        }

    /* ======================
       path
       ====================== */


        public static String reportsPath() {
            return PropertyReader.get("report_logs_path", String.class);
        }

    /* ======================
       Email
       ====================== */

        public static String emailTo() {
            return PropertyReader.get("Email_to", String.class);
        }

        public static String emailCc() {
            return PropertyReader.get("Email_CC", String.class);
        }

        public static String emailSubject() {
            return PropertyReader.get("Email_subject", String.class);
        }

        public static String emailBody() {
            return PropertyReader.get("Email_body", String.class);
        }

        public static String emailAttachmentPath() {
            return PropertyReader.get("Email_attachment_path", String.class);
        }

        // Gmail
        public static String gmailToken() {
            return PropertyReader.get("gmail_token_path", String.class);
        }

    //Paths

    }

