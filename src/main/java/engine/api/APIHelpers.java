package engine.api;

import engine.reporters.Loggers;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.response.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class APIHelpers {

    protected static int castObjectToIntOrDefault(Object data, int defaultValue){

        if (data == null) return defaultValue;
        try {
            if (data instanceof Number n) return n.intValue();
            if (data instanceof String s) return Integer.parseInt(s.trim());
            Loggers.log.warn("Cannot convert type [{}] to int; returning default {}", data.getClass(), defaultValue);
        } catch (NumberFormatException ex) {
            Loggers.log.warn("Failed to parse integer from [{}]; returning default {}", data, defaultValue);
        }
        return defaultValue;

    }


    public enum AuthType { BASIC, BEARER, DIGEST, OAUTH1 }

    protected static AuthenticationScheme setAuthorizationScheme(AuthType type, String... parts){
        try{
            return switch (type) {
                case BASIC -> {
                    guardAuthorization(parts, 2);
                    Loggers.log.info("Setting BASIC authorization for user [{}]", parts[0]);
                    yield RestAssured.preemptive().basic(parts[0], parts[1]);
                }
                case BEARER -> {
                    guardAuthorization(parts, 1);
                    Loggers.log.info("Setting BEARER authorization");
                    yield RestAssured.oauth2(parts[0]);
                }
                case DIGEST -> {
                    guardAuthorization(parts, 2);
                    Loggers.log.info("Setting DIGEST authorization for user [{}]", parts[0]);
                    yield RestAssured.digest(parts[0], parts[1]);
                }
                case OAUTH1 -> {
                    guardAuthorization(parts, 4);
                    Loggers.log.info("Setting OAUTH1 authorization ");
                    yield RestAssured.oauth(parts[0], parts[1], parts[2], parts[3]);
                }
            }; }
        catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Invalid auth configuration");
        }
    }

    private static void guardAuthorization(String[] parts, int expected) {
        if (parts == null || parts.length < expected) {
            throw new IllegalArgumentException("Insufficient auth parts; expected " + expected);
        }
    }


    public static Response waitForResponse(CompletableFuture<Response> res){
        Response response;
        try {
            response = res.get();
            Loggers.log.info("Cast future response into response type");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
