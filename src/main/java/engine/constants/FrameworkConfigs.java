package engine.constants;

import engine.utils.PropertyReader;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FrameworkConfigs {


    /* ======================
       Core resolver
       ====================== */

  
    /* ======================
       Typed accessors
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

        public static String baseUrl() {
            return PropertyReader.get("mainurl", String.class);
        }
    public static String automationPlayGroundURL() {
        return PropertyReader.get("testAutomationPlayGroundLink", String.class);
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
            return PropertyReader.get("seleniumGrid", Boolean.class);
        }

        public static boolean localPathDriver() {
            return PropertyReader.get("localDriver", Boolean.class);
        }

        public static String proxy() {
            return PropertyReader.get("proxy", String.class);
        }

        public static String downloadsPath() {
            return PropertyReader.get("downloaded_files_Path", String.class);
        }
        public static boolean per_test_log() {
            return PropertyReader.get("per_test_log", Boolean.class);
        }
        public static String gridPath() {
            return PropertyReader.get("gridPath", String.class);
        }

    /* ======================
       Reporting
       ====================== */

        public static boolean openAllure() {
            return PropertyReader.get("openAllureAfterTest", Boolean.class);
        }

        public static String allureBat() {
            return PropertyReader.get("allure_bat_file_path", String.class);
        }

        public static String allureGenerationPath() {
            return PropertyReader.get("allure_generation_path", String.class);
        }

        public static String allureCompressionPath() {
            return PropertyReader.get("allure_compression_path", String.class);
        }

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

        public static boolean sendReportEmail() {
            return PropertyReader.get("generateAndSendReport", Boolean.class);
        }

        // Gmail
        public static String gmailToken() {
            return PropertyReader.get("gmail_token_path", String.class);
        }
    public static String gmailCredentials() {
        return PropertyReader.get("gmail_credentials", String.class);
    }

    }

