package engine.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.reporters.Loggers;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;

public class ResponseActions {
    private ResponseActions(){}
    private static final ObjectMapper objectMapper;

    static {
        objectMapper=new ObjectMapper();
    }
    public static void checkResponseStatus(Response res,int status){
        if(res.statusCode()!=status && Loggers.getLogger().isErrorEnabled()){
                String error = ResponseActions.getValueByPath(res, "error", String.class);
                Loggers.logError(
                        "Incorrect status code. Getting error: "+error);
        }
        res.then().statusCode(status);
        Loggers.logInfo("Validate that status code = "+status);
    }

    public static void checkResponseContent(Response res, String contentType){
        res.then().contentType(contentType);
        Loggers.logInfo("Validate that contentType = "+contentType);
    }

    public static void checkResponseCookie( Response res,String cookieName, String value){
        res.then().cookie(cookieName,value);
        Loggers.logInfo("Validate that cookie  "+cookieName+" == "+value);
    }

    public static void checkResponseHeader( Response res,String headerName, String value){
        res.then().header(headerName,value);
        Loggers.logInfo("Validate that header "+headerName+" == "+value);
    }

    public static void logResponse(Response response){
        Loggers.logInfo("log all response");
        response.then().log().all();
    }
    public static void logBody(Response response){
        Loggers.logInfo("log body only");
        response.then().log().body();
    }

    public static void logHeaders(Response response){
        Loggers.logInfo("log all headers");
        response.then().log().headers();
    }

    public static void logCookies(Response response){
        Loggers.logInfo("log all cookies");
        response.then().log().cookies();
    }
    public static String getBodyAsString(Response response){
        Loggers.logInfo("get body");
        return response.body().asString();
    }

    public static Headers getAllHeaders(Response response){
        Loggers.logInfo("get all headers");
        return response.getHeaders();
    }
    public static String getCertainHeader(Response response,String header){
        Loggers.logInfo("get header with the name : "+header);
        return response.getHeader(header);
    }

    public static Map<String, String> getAllCookies(Response response){
        Loggers.logInfo("get all cookies");
        return response.getCookies();
    }
    public static String getCertainCookie(Response response,String value){
        Loggers.logInfo("get certain cookie");
        return response.getCookie(value);
    }

    public static <T> T getValueByPath(Response response, String path, Class<T> type) {
        return response.jsonPath().getObject(path, type);
    }

    public static <T> T getValueByPath(Response response, String path, TypeRef<T> typeRef) {
        return response.jsonPath().getObject(path, typeRef);
    }

    public static ExtractableResponse<Response> getResponse(Response response){
        Loggers.logInfo("get full response");
        return response.then().extract();
    }

    public static JsonPath getFullJsonPath(Response response){
        Loggers.logInfo("get only json path");
        return response.body().jsonPath();
    }

    public static <T> T deserializeResponseIntoClass(Response response, Class<T> className){
        try{
            T object = objectMapper.readValue(response.getBody().asString(), className);
            Loggers.logInfo("read json from current response and deserialize it into class "+className.getSimpleName() );
            return object;
        }
        catch (Exception e){
            Loggers.logInfo("Couldn't deserialize current response");
            return null;
        }
    }
}
