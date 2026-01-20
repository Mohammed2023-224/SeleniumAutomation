package engine.actions;

import engine.reporters.Loggers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HTTPActions {
    URL url ;
    HttpURLConnection connection;

    public HTTPActions (String url){
        try {
            this.url = new URL(url);
            this.connection = (HttpURLConnection) this.url.openConnection();
        } catch (Exception e) {
            Loggers.getLogger().error("Failed to create HTTP connection");
        }
    }

    public HTTPActions setMethod(String method){
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            Loggers.getLogger().error("Failed to set HTTP method");
        }
        return this;
    }

    public HTTPActions setHeaders(Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(connection::setRequestProperty);
        }
        return this;
    }
    public HTTPActions startConnection(){
        try {
             connection.connect();
        } catch (IOException e) {
            Loggers.getLogger().error("Failed to connect");
        }
        return this;
    }

    public String getContentDisposition(){
      return  connection.getHeaderField("Content-Disposition");
    }
    public long getContentLength(){
      return  connection.getContentLengthLong();
    }


    public int getResponseCode(){
        int resCode = 0;
        try {
            resCode=connection.getResponseCode();
        } catch (IOException e) {
            Loggers.getLogger().error("Failed to get response code");
        }
        return resCode;
    }
    public InputStream getResponseStream() {
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            try {
                return connection.getErrorStream();
            } catch (Exception ex) {
                throw new RuntimeException("No response stream available", ex);
            }
        }
    }
}
