package engine.actions;

import engine.reporters.Loggers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HTTPActions {
    URL url ;
    HttpURLConnection connection;

    public HTTPActions (String url){
        try {
            this.url = new URL(url);
            this.connection = (HttpURLConnection) this.url.openConnection();
        } catch (Exception e) {
            Loggers.log.error("Failed to create HTTP connection");
        }
    }

    public HTTPActions setMethod(String method){
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            Loggers.log.error("Failed to set HTTP method");
        }
        return this;
    }

    public HTTPActions startConnection(){
        try {
            connection.connect();
        } catch (IOException e) {
            Loggers.log.error("Failed to connect");
        }
        return this;
    }


    public int getResponseCode(){
        int resCode = 0;
        try {
            resCode=connection.getResponseCode();
        } catch (IOException e) {
            Loggers.log.error("Failed to get response code");
        }
        return resCode;
    }
}
