package engine.actions;

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
            throw new RuntimeException(e);
        }
    }

    public HTTPActions setMethod(String method){
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public HTTPActions startConnection(){
        try {
            connection.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    public int getResponseCode(){
        int resCode;
        try {
            resCode=connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resCode;
    }
}
