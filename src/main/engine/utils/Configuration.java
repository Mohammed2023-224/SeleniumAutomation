package engine.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public   class Configuration {
    @JsonProperty("Configs")
    Object configs;

    public Config getSingleUserData() {
        return  JSONReader.getSingleData(configs,Config.class);
    }

    @SuppressWarnings("unchecked")
    public List<Config> getUserList() {
        return  JSONReader.getDataInList(configs, Config.class);
    }

    @Getter
    @Setter
    public static class Config {
        @JsonProperty("Headless")
        private String headless;

        @JsonProperty("Maximized")
        private String maximized;
    }

}
