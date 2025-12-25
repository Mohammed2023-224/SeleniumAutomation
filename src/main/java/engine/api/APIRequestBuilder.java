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

    public APIRequestBuilder(APIRequestBuilder api) {
        this.requestSpecBuilder = new RequestSpecBuilder();
        setURL(api.url); // copy base URL
        setCookies(api.cookies); // copy cookies
        setHeaders(api.headers); // copy cookies
    }

    public APIRequestBuilder() {
    }

    public APIRequestBuilder(String url, Map<String, String> cookies) {
        setURL(url);
        setCookies(cookies);
    }


    private Response sendRequest(HttpMethods method) {
        return switch (method) {
            case GET -> RestAssured.given().spec(requestSpecBuilder.build()).when().get();
            case POST -> RestAssured.given().spec(requestSpecBuilder.build()).when().post();
            case PUT -> RestAssured.given().spec(requestSpecBuilder.build()).when().put();
            case PATCH -> RestAssured.given().spec(requestSpecBuilder.build()).when().patch();
            case DELETE -> RestAssured.given().spec(requestSpecBuilder.build()).when().delete();
        };
    }

    public Response performRequest(HttpMethods requestType) {
        Loggers.log.info("Start executing sync {} request", requestType.getMethod());
        return sendRequest(requestType);
    }


    public CompletableFuture<Response> performAsyncRequest(HttpMethods requestType, int waitTime, int pollTime, int statusCode) {
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


    public void setURL(String url) {
        requestSpecBuilder.setBaseUri(url);
        this.url = url;
        Loggers.log.info("Set base URl to [{}]", url);
    }

    public void setAuthorization(APIHelpers.AuthType authType, String... info) {
        requestSpecBuilder.setAuth(APIHelpers.setAuthorizationScheme(authType, info));
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        if (headers != null && !headers.isEmpty()) {
            this.headers.putAll(headers);
            requestSpecBuilder.addHeaders(this.headers);
            Loggers.log.info("set headers: {}", this.headers);
        }
    }

    public void setHeaders(String headerName, String headerValue) {
        requestSpecBuilder.addHeader(headerName, headerValue);
        Loggers.log.info("Add header [{}] -> [{}]", headerName, headerValue);
    }

    public void addQueryParam(Map<String, String> queryParams) {
        if (queryParams != null) {
            queryParams.forEach((k, v) -> {
                requestSpecBuilder.addQueryParam(k, v);
                Loggers.log.info("Query param [{}]=[masked]", k);
            });
        }
    }

    public void addQueryParam(String queryName, String value) {
        requestSpecBuilder.addQueryParam(queryName, value);
        Loggers.log.info("add query parameters: {} -> {}", queryName, value);
    }

    public void setBasePathParameter(String path) {
        requestSpecBuilder.setBasePath(path);
        Loggers.log.info("Set base path parameter to [{}]", path);
    }

    public void setProxy(int proxy) {
        requestSpecBuilder.setProxy(proxy);
        Loggers.log.info("Set proxy to [{}]", proxy);
    }


    public void setCookies(Map<String, String> cookies) {
        this.cookies.clear();
        if (cookies != null && !cookies.isEmpty()) {
            this.cookies.putAll(cookies); // copy instead of assigning
            requestSpecBuilder.addCookies(this.cookies);
            System.out.println("Cookies set: " + this.cookies.keySet());
        }
    }

    public void setCookies(String cookies) {
        requestSpecBuilder.addCookie(cookies);
        Loggers.log.info("Add cookie to [{}]", cookies);
    }

    public void setContentTypeAndAccept(String contentType) {
        requestSpecBuilder.setContentType(contentType).setAccept(contentType);
        Loggers.log.info("Set content type to [{}]", contentType);
    }

    public void setBodyAsFile(String filePath, String contentType) {
        try {
            setContentTypeAndAccept(contentType);
            requestSpecBuilder.setBody(Files.readAllBytes(Paths.get(filePath)));
            Loggers.log.info("Set body as file located at [{}]", filePath);
        } catch (IOException e) {
            Loggers.log.info("couldn't set body as file located at [{}]", filePath);
            throw new RuntimeException(e);
        }
    }

    public void setBodyAsString(String body) {
        requestSpecBuilder.setBody(body);
        Loggers.log.info("Set body as string: [{}]", body);
    }

    public void setBodyAsObject(Object body) {
        requestSpecBuilder.setBody(body);
        Loggers.log.info("Set body as current object [{}]", body);
    }

    public void addFormParams(HashMap<String, String> header) {
        requestSpecBuilder.addFormParams(header);
    }
}