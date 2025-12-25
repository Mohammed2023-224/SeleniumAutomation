package engine.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.reporters.Loggers;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Map;

public class ResponseActions {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper=new ObjectMapper();
    }
    public static void checkResponseStatus(Response res,int status){
        res.then().statusCode(status);
        Loggers.log.info("Validate that status code = {}",status);
    }

    public static void checkResponseContent(Response res, String contentType){
        res.then().contentType(contentType);
        Loggers.log.info("Validate that contentType = {}",contentType);
    }

    public static void checkResponseCookie( Response res,String cookieName, String value){
        res.then().cookie(cookieName,value);
        Loggers.log.info("Validate that cookie  {} == {}",cookieName,value);
    }

    public static void checkResponseHeader( Response res,String headerName, String value){
        res.then().header(headerName,value);
        Loggers.log.info("Validate that header  {} == {}",headerName,value);
    }

    public static void logResponse(Response response){
        Loggers.log.info("log all response");
        response.then().log().all();
    }
    public static void logBody(Response response){
        Loggers.log.info("log body only");
        response.then().log().body();
    }

    public static void logHeaders(Response response){
        Loggers.log.info("log all headers");
        response.then().log().headers();
    }

    public static void logCookies(Response response){
        Loggers.log.info("log all cookies");
        response.then().log().cookies();
    }
    public static String getBodyAsString(Response response){
        Loggers.log.info("get body");
        return response.body().asString();
    }

    public static Headers getAllHeaders(Response response){
        Loggers.log.info("get all headers");
        return response.getHeaders();
    }
    public static String getCertainHeader(Response response,String header){
        Loggers.log.info("get header: "+header);
        return response.getHeader(header);
    }

    public static Map<String, String> getAllCookies(Response response){
        Loggers.log.info("get all cookies");
        return response.getCookies();
    }
    public static String getCertainCookie(Response response,String value){
        Loggers.log.info("get certain cookie");
        return response.getCookie(value);
    }

    public static <T> T getValueByPath(Response response, String path, Class<T> type) {
        return response.jsonPath().getObject(path, type);
    }

    public static <T> T getValueByPath(Response response, String path, TypeRef<T> typeRef) {
        return response.jsonPath().getObject(path, typeRef);
    }

    public static ExtractableResponse<Response> getResponse(Response response){
        Loggers.log.info("get full response");
        return response.then().extract();
    }

    public static JsonPath getFullJsonPath(Response response){
        Loggers.log.info("get only json path");
        return response.body().jsonPath();
    }

    public static <T> T deserializeResponseIntoClass(Response response, Class<T> className){
        try{
            T object = objectMapper.readValue(response.getBody().asString(), className);
            Loggers.log.info("read json from current response and deserialize it into class [{}]",className.getSimpleName() );
            return object;
        }
        catch (Exception e){
            Loggers.log.info("Couldn't deserialize current response");
            throw new RuntimeException(e);
        }
    }

    public static <T> void assertEquals(T expected, T actual ){
        if( expected.equals(actual) ){
            Loggers.log.info("Value {} is equal to actual {}",expected,actual);
        }
        else {
            Loggers.log.info("Value {} is not equal to actual {}",expected,actual);
        }
    }
}
