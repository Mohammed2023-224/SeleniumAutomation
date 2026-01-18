package engine.driver;

import engine.reporters.Loggers;

import java.net.HttpURLConnection;
import java.net.URL;

public class DriverHelper {
    public static void waitForRemoteUrl(String url, int timeoutSeconds) {
        Loggers.getLogger().info("Checking url: "+url);
        long end = System.currentTimeMillis() + timeoutSeconds * 1000;
        while (System.currentTimeMillis() < end) {
            try {
                HttpURLConnection con =
                        (HttpURLConnection) new URL(url+"/status").openConnection();
                con.setConnectTimeout(1000);
                if (con.getResponseCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {}
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        throw new RuntimeException("Grid not ready");
    }
}
