package engine.api;

import engine.reporters.Loggers;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import org.awaitility.Awaitility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class APIRequestBuilder {
    private String url;
    private LinkedHashMap<String, String> cookies = new LinkedHashMap<>();
    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

    // ✅ Copy constructor
    public APIRequestBuilder(APIRequestBuilder api) {
        this.requestSpecBuilder = new RequestSpecBuilder();
        setURL(api.url); // copy base URL
        setCookies(api.cookies); // copy cookies
        setHeaders(api.headers); // copy cookies
    }

    public APIRequestBuilder() {}

    public APIRequestBuilder(String url, Map<String, String> cookies) {
        setURL(url);
        setCookies(cookies);
    }



    private Response sendRequest(String requestType) {
        switch (requestType.toLowerCase()) {
            case "get":
                return RestAssured.given().spec(requestSpecBuilder.build()).when().get();
            case "post":
                return RestAssured.given().spec(requestSpecBuilder.build()).when().post();
            case "put":
                return RestAssured.given().spec(requestSpecBuilder.build()).when().put();
            case "patch":
                return RestAssured.given().spec(requestSpecBuilder.build()).when().patch();
            case "delete":
                return RestAssured.given().spec(requestSpecBuilder.build()).when().delete();
            default:
                throw new IllegalArgumentException("Unsupported request type: " + requestType);
        }
    }

    public Response performRequest(HttpMethods requestType) {
        Loggers.log.info("Start executing sync {} request", requestType.getMethod());
        return sendRequest(requestType.getMethod());
    }


    public CompletableFuture<Response> performAsyncRequest(String requestType, int waitTime, int pollTime, int statusCode) {
        return CompletableFuture.supplyAsync(() -> {
            Loggers.log.info("Start executing async {} request", requestType);
            final Response[] finalResponse = {null};
            Awaitility.await()
                    .atMost(waitTime, TimeUnit.SECONDS)
                    .pollInterval(pollTime, TimeUnit.SECONDS)
                    .until(() -> {
                        Response res = sendRequest(requestType);
                        finalResponse[0] = res;
                        return res.getStatusCode() == statusCode;
                    });

            return finalResponse[0];
        });
    }


    public void setURL(String url){
        requestSpecBuilder.setBaseUri(url);
        this.url = url;
        Loggers.log.info("Set base URl to [{}]",url);
    }

    public void setAuthorization(String... info){
        requestSpecBuilder.setAuth(APIHelpers.setAuthorizationScheme(info));
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        if (headers != null && !headers.isEmpty()) {
            this.headers.putAll(headers); // copy instead of assigning
            requestSpecBuilder.addHeaders(this.headers);
            Loggers.log.info("set headers: {}", this.headers);
        }
    }


    public void addQueryParams(Map<String,String> queryParams){
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            requestSpecBuilder.addQueryParam(entry.getKey(), entry.getValue());
            Loggers.log.info("set query parameters: {} -> {}",entry.getKey(), entry.getValue());
        }
    }

    public void addQueryParam(String queryName, String value){
        requestSpecBuilder.addQueryParam(queryName,value);
        Loggers.log.info("add query parameters: {} -> {}",queryName,value);
    }

    public void setPathParams(String path){
        requestSpecBuilder.setBasePath(path);
        Loggers.log.info("Set base path parameter to [{}]",path);
    }

    public void setProxy(int proxy){
        requestSpecBuilder.setProxy(proxy);
        Loggers.log.info("Set proxy to [{}]",proxy);
    }


    public void setCookies(Map<String, String> cookies) {
        this.cookies.clear();
        if (cookies != null && !cookies.isEmpty()) {
            this.cookies.putAll(cookies); // copy instead of assigning
            requestSpecBuilder.addCookies(this.cookies);
            System.out.println("Cookies set: " + this.cookies);
        }
    }

    public void clearCookies(){
        Map<String, String> LinkedHashMap = new LinkedHashMap<>();
        requestSpecBuilder.addCookies(LinkedHashMap);
    }
    public void clearHeaders(){
        Map<String, String> LinkedHashMap = new LinkedHashMap<>();
        requestSpecBuilder.addHeaders(LinkedHashMap);
    }


    public void addCookie(String cookies){
        requestSpecBuilder.addCookie(cookies);
        Loggers.log.info("Add cookie to [{}]",cookies);
    }

    public void addHeader(String headerName,String headerValue){
        requestSpecBuilder.addHeader(headerName,headerValue);
        Loggers.log.info("Add header [{}] -> [{}]",headerName,headerValue);
    }

    public void setContentTypeAndAccept(String contentType){
        requestSpecBuilder.setContentType(contentType).setAccept(contentType);
        Loggers.log.info("Set content type to [{}]",contentType);
    }

    public void setBodyAsFile(String filePath){
        try {
            requestSpecBuilder.addHeader("Content-Type","application/json");
            requestSpecBuilder.addHeader("Accept","application/json");
            requestSpecBuilder.setBody(Files.readAllBytes(Paths.get(filePath)));
            Loggers.log.info("Set body as file located at [{}]", filePath);
        } catch (IOException e) {
            Loggers.log.info("couldn't set body as file located at [{}]", filePath);
            throw new RuntimeException(e);
        }
    }

    public void setBodyAsString(String body){
        requestSpecBuilder.setBody(body);
        Loggers.log.info("Set body as string: [{}]",body);
    }
    public void setBodyAsObject(Object body){
        requestSpecBuilder.setBody(body);
        Loggers.log.info("Set body as current object [{}]",body);
    }
    public void addFormParams(HashMap<String,String> header){
        requestSpecBuilder.addFormParams(header);
    }
}
