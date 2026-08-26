package engine.driver;

import engine.exceptions.CustomExceptions;
import engine.reporters.Loggers;
import java.net.HttpURLConnection;
import java.net.URI;

public class DriverHelper {
    private DriverHelper(){}
    public static void waitForRemoteUrl(String url, int timeoutSeconds) {
        Loggers.logInfo("Checking url: " + url);
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < end) {
            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection)
                        new URI(url + "/status").toURL().openConnection();

                con.setConnectTimeout(1000);
                con.setReadTimeout(1000);
                if (con.getResponseCode() == 200) {
                    Loggers.logInfo("Remote URL is available: " + url);
                    return;
                }
            } catch (Exception e) {
                Loggers.logInfo("Remote URL not available yet: " + url);
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Loggers.logError("Thread interrupted while waiting for remote URL: " + url);
                throw new CustomExceptions("Interrupted while waiting for remote URL");
            }
        }

        throw new CustomExceptions(
                "Remote URL did not become available within "
                        + timeoutSeconds + " seconds: " + url
        );
    }
}