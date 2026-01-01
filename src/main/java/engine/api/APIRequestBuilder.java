package engine.api;

import engine.reporters.Loggers;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.Marker;
import org.awaitility.Awaitility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class APIRequestBuilder {

    private String url;
    private LinkedHashMap<String, String> cookies = new LinkedHashMap<>();
    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

    public APIRequestBuilder(APIRequestBuilder api,boolean url,boolean cookies,boolean headers) {
        this.requestSpecBuilder = new RequestSpecBuilder();
        if (url) setURL(api.url);
        if (cookies) setCookies(api.cookies);
        if (headers) setHeaders(api.headers);
    }

    public APIRequestBuilder() {
    }
    public APIRequestBuilder(String url) {
        setURL(url);
    }
    public APIRequestBuilder(String url, Map<String, String> cookies) {
        setURL(url);
        setCookies(cookies);
    }
    public APIRequestBuilder(String url, Map<String, String> cookies,Map<String, String>  headers) {
        setURL(url);
        setCookies(cookies);
        setHeaders(headers);
    }


    private Response sendRequest(HttpMethods method) {
        RequestSpecification spec = RestAssured.given().spec(requestSpecBuilder.build());
        return switch (method) {
            case GET ->spec.when().get();
            case POST -> spec.when().post();
            case PUT -> spec.when().put();
            case PATCH -> spec.when().patch();
            case DELETE -> spec.when().delete();
        };
    }

    public Response performRequest(HttpMethods requestType) {
        Loggers.getLogger().info("Start executing sync {} request", requestType.getMethod());
        return sendRequest(requestType);
    }


    public CompletableFuture<Response> performAsyncRequest(HttpMethods requestType, int waitTime, int pollTime, int statusCode) {
        return CompletableFuture.supplyAsync(() -> {
            Loggers.getLogger().info("Start executing async {} request", requestType);
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


    public void setURL(String url) {
        requestSpecBuilder.setBaseUri(url);
        this.url = url;
        Loggers.getLogger().info("Set base URl to [{}]", url);
    }

    public void setAuthorization(APIHelpers.AuthType authType, String... info) {
        requestSpecBuilder.setAuth(APIHelpers.setAuthorizationScheme(authType, info));
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        if (headers != null && !headers.isEmpty()) {
            this.headers.putAll(headers);
            requestSpecBuilder.addHeaders(this.headers);
            Loggers.getLogger().info("set headers: {}", this.headers);
        }
    }

    public void setHeaders(String headerName, String headerValue) {
        requestSpecBuilder.addHeader(headerName, headerValue);
        Loggers.getLogger().info("Add header [{}] -> [{}]", headerName, headerValue);
    }

    public void addQueryParam(Map<String, String> queryParams) {
        if (queryParams != null) {
            queryParams.forEach((k, v) -> {
                requestSpecBuilder.addQueryParam(k, v);
                Loggers.getLogger().info("Query param [{}]=[masked]", k);
            });
        }
    }
    public void addQueryParam(String name, List<String> queryParams) {
        if (queryParams != null) {
            requestSpecBuilder.addQueryParam(name,queryParams);
        }
    }

    public void addQueryParam(String queryName, String value) {
        requestSpecBuilder.addQueryParam(queryName, value);
        Loggers.getLogger().info("add query parameters: {} -> {}", queryName, value);
    }

    public void setBasePathParameter(String path) {
        requestSpecBuilder.setBasePath(path);
        Loggers.getLogger().info("Set base path parameter to [{}]", path);
    }

    public void setProxy(String proxy) {
        requestSpecBuilder.setProxy(proxy);
        Loggers.getLogger().info("Set proxy to [{}]", proxy);
    }


    public void setCookies(Map<String, String> cookies) {
        this.cookies.clear();
        if (cookies != null && !cookies.isEmpty()) {
            this.cookies.putAll(cookies); // copy instead of assigning
            requestSpecBuilder.addCookies(this.cookies);
            System.out.println("Cookies set: " + this.cookies.keySet());
        }
    }

    public void setCookies(String cookies,String value) {
        requestSpecBuilder.addCookie(cookies,value);
        Loggers.getLogger().info("Add cookie {} with value {}", cookies,value);
    }

    public void setContentTypeAndAccept(String contentType) {
        requestSpecBuilder.setContentType(contentType).setAccept(contentType);
        Loggers.getLogger().info("Set content type to [{}]", contentType);
    }

    public Marker logRequest() {
        requestSpecBuilder.log(LogDetail.ALL);
        return null;
    }

    public void setBodyAsFile(String filePath, String contentType) {
        try {
            setContentTypeAndAccept(contentType);
            requestSpecBuilder.setBody(Files.readAllBytes(Paths.get(filePath)));
            Loggers.getLogger().info("Set body as file located at [{}]", filePath);
        } catch (IOException e) {
            Loggers.getLogger().info("couldn't set body as file located at [{}]", filePath);
            throw new RuntimeException(e);
        }
    }

    public void setBodyAsString(String body) {
        requestSpecBuilder.setBody(body);
        Loggers.getLogger().info("Set body as string: [{}]", body);
    }

    public void setBodyAsObject(Object body) {
        requestSpecBuilder.setBody(body);
        Loggers.getLogger().info("Set body as current object [{}]", body);
    }

    public void addFormParams(HashMap<String, String> header) {
        requestSpecBuilder.addFormParams(header);
    }
}