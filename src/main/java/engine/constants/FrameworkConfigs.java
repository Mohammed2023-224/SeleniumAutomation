package engine.constants;

import engine.utils.PropertyReader;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FrameworkConfigs {

        private static final Map<String, String> CACHE = new ConcurrentHashMap<>();
        private static Properties properties;
    /* ======================
       Core resolver
       ====================== */

        private static String get(String key) {
            if(properties ==null) properties= PropertyReader.readAllProperties();
            return CACHE.computeIfAbsent(key, k -> {
                String sys = System.getProperty(k);
                if (sys != null && !sys.isBlank()) {
                    return sys;
                }
                return PropertyReader.readProp(k);
            });
        }
        private static boolean getBoolean(String key){
            return Boolean.parseBoolean(get(key));
        }
        private static int getInt(String key){
            return Integer.getInteger(get(key));
        }

    /* ======================
       Typed accessors
       ====================== */

        public static int shortWait() {
            return Integer.parseInt(get("shortWaitTime"));
        }

        public static int longWait() {
            return Integer.parseInt(get("longWaitTime"));
        }

        public static int retryCount() {
            return Integer.parseInt(get("RetryCount"));
        }

        public static String baseUrl() {
            return get("mainurl");
        }
    public static String automationPlayGroundURL() {
        return get("testAutomationPlayGroundLink");
    }
        public static String browser() {
            return get("Browser");
        }

        public static boolean headless() {
            return Boolean.parseBoolean(get("headless"));
        }
        public static boolean popupBlocker() {
            return Boolean.parseBoolean(get("headless"));
        }

        public static boolean maximized() {
            return Boolean.parseBoolean(get("maximized"));
        }

        public static boolean localExecution() {
            return Boolean.parseBoolean(get("local_execution"));
        }
        public static String testDataPath() {
            return get("test_data_path");
        }

        public static boolean gridEnabled() {
            return Boolean.parseBoolean(get("seleniumGrid"));
        }

        public static boolean localPathDriver() {
            return Boolean.parseBoolean(get("localDriver"));
        }

        public static String proxy() {
            return get("proxy");
        }

        public static String downloadsPath() {
            return get("downloaded_files_Path");
        }
        public static boolean per_test_log() {
            return getBoolean("per_test_log");
        }
        public static String gridPath() {
            return get("gridPath");
        }
        public static String extraLogFileToDelete() {
            return get("extra_log_file_to_delete");
        }

    /* ======================
       Reporting
       ====================== */

        public static boolean openAllure() {
            return Boolean.parseBoolean(get("openAllureAfterTest"));
        }

        public static String allureBat() {
            return get("allure_bat_file_path");
        }

        public static String allureGenerationPath() {
            return get("allure_generation_path");
        }

        public static String allureCompressionPath() {
            return get("allure_compression_path");
        }

        public static String reportsPath() {
            return get("report_logs_path");
        }

    /* ======================
       Email
       ====================== */

        public static String emailTo() {
            return get("Email_to");
        }

        public static String emailCc() {
            return get("Email_CC");
        }

        public static String emailSubject() {
            return get("Email_subject");
        }

        public static String emailBody() {
            return get("Email_body");
        }

        public static String emailAttachmentPath() {
            return get("Email_attachment_path");
        }

        public static boolean sendReportEmail() {
            return Boolean.parseBoolean(get("generateAndSendReport"));
        }

        // Gmail
        public static String gmailToken() {
            return get("gmail_token_path");
        }
    public static String gmailCredentials() {
        return get("gmail_credentials");
    }

    }

//}
