package engine.utils;

import engine.utils.propertyFilesHandlers.PropertyReader;

public class EnvSelector {
    public static String envSelector(boolean  apiURL) {

        String env = System.getProperty("env",PropertyReader.get("environment", String.class));
        String baseKey = env+"URL";
        if (apiURL) {
            String baseUrl = PropertyReader.get(baseKey, String.class);
            String apiPath = PropertyReader.get("api_path", String.class);
            return baseUrl + apiPath;
        } else {
            return PropertyReader.get(baseKey, String.class);
        }
    }
}
