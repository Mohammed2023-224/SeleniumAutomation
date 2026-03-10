package engine.driver;

import engine.reporters.Loggers;

import java.net.HttpURLConnection;
import java.net.URI;

public class DriverHelper {
    private DriverHelper(){}
    public static void waitForRemoteUrl(String url, int timeoutSeconds) {
        Loggers.getLogger().info("Checking url: {}",url);
        long end = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < end) {
            try {
                HttpURLConnection con =
                        (HttpURLConnection) new URI(url+"/status").toURL().openConnection();
                con.setConnectTimeout(1000);
                if (con.getResponseCode() == 200) {
                    return;
                }
                Thread.sleep(1000);
            }
            catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            catch (Exception e)
            {Loggers.getLogger().error("Not handled error",e);}
        }
    }
}
