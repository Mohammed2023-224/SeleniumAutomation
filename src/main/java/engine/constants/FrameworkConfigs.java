package engine.constants;

import engine.utils.PropertyReader;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FrameworkConfigs {
    static final Properties PROPS = PropertyReader.readAllProperties();

        private static final Map<String, String> CACHE = new ConcurrentHashMap<>();

    /* ======================
       Core resolver
       ====================== */

    private static String resolve(String key) {
        return CACHE.computeIfAbsent(key, k -> {

            String sysValue = System.getProperty(k);
            if (sysValue != null && !sysValue.isBlank()) {
                return sysValue;
            }
            String fileValue = PropertyReader.readProp(k);

            if (fileValue == null) {
                throw new IllegalStateException(
                        "Missing configuration key: " + k
                );
            }

            return fileValue.trim();
        });
    }
    public static <T> T get(String key, Class<T> type) {
        String value = resolve(key);
        if (value == null) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        try {
            if (type == String.class) {
                return type.cast(value);
            }
            if (type == Integer.class) {
                return type.cast(Integer.parseInt(value));
            }
            if (type == Boolean.class) {
                return type.cast(Boolean.parseBoolean(value));
            }
            if (type == Long.class) {
                return type.cast(Long.parseLong(value));
            }
            if (type == Double.class) {
                return type.cast(Double.parseDouble(value));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to parse config key '" + key +
                            "' as " + type.getSimpleName() +
                            " (value=" + value + ")",
                    e
            );
        }
        throw new IllegalArgumentException(
                "Unsupported type: " + type.getName()
        );
    }
    /* ======================
       Typed accessors
       ====================== */

        public static int shortWait() {
            return get("shortWaitTime", Integer.class);
        }

        public static int longWait() {
            return get("longWaitTime", Integer.class);
        }

        public static int retryCount() {
            return get("RetryCount", Integer.class);
        }

        public static String baseUrl() {
            return get("mainurl", String.class);
        }
    public static String automationPlayGroundURL() {
        return get("testAutomationPlayGroundLink", String.class);
    }
        public static String browser() {
            return get("Browser", String.class);
        }

        public static boolean headless() {
            return get("headless", Boolean.class);
        }
        public static boolean popupBlocker() {
            return get("headless", Boolean.class);
        }

        public static boolean maximized() {
            return get("maximized", Boolean.class);
        }

        public static boolean localExecution() {
            return get("local_execution", Boolean.class);
        }
        public static String testDataPath() {
            return get("test_data_path", String.class);
        }

        public static boolean gridEnabled() {
            return get("seleniumGrid", Boolean.class);
        }

        public static boolean localPathDriver() {
            return get("localDriver", Boolean.class);
        }

        public static String proxy() {
            return get("proxy", String.class);
        }

        public static String downloadsPath() {
            return get("downloaded_files_Path", String.class);
        }
        public static boolean per_test_log() {
            return get("per_test_log", Boolean.class);
        }
        public static String gridPath() {
            return get("gridPath", String.class);
        }
        public static String extraLogFileToDelete() {
            return get("extra_log_file_to_delete", String.class);
        }

    /* ======================
       Reporting
       ====================== */

        public static boolean openAllure() {
            return get("openAllureAfterTest", Boolean.class);
        }

        public static String allureBat() {
            return get("allure_bat_file_path", String.class);
        }

        public static String allureGenerationPath() {
            return get("allure_generation_path", String.class);
        }

        public static String allureCompressionPath() {
            return get("allure_compression_path", String.class);
        }

        public static String reportsPath() {
            return get("report_logs_path", String.class);
        }

    /* ======================
       Email
       ====================== */

        public static String emailTo() {
            return get("Email_to", String.class);
        }

        public static String emailCc() {
            return get("Email_CC", String.class);
        }

        public static String emailSubject() {
            return get("Email_subject", String.class);
        }

        public static String emailBody() {
            return get("Email_body", String.class);
        }

        public static String emailAttachmentPath() {
            return get("Email_attachment_path", String.class);
        }

        public static boolean sendReportEmail() {
            return get("generateAndSendReport", Boolean.class);
        }

        // Gmail
        public static String gmailToken() {
            return get("gmail_token_path", String.class);
        }
    public static String gmailCredentials() {
        return get("gmail_credentials", String.class);
    }

    }

