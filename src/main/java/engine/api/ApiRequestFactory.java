package engine.api;

import engine.reporters.Loggers;
import io.restassured.response.Response;

import java.util.function.Function;

public class ApiRequestFactory {
    private final String baseUrl;
    private final TokenProvider tokenProvider;

    public ApiRequestFactory(String baseUrl, TokenProvider tokenProvider) {
        this.baseUrl = baseUrl;
        this.tokenProvider = tokenProvider;
    }


    public APIRequestBuilder newRequest() {
        AuthSession session = tokenProvider.getSession();
        return new APIRequestBuilder(baseUrl, session.cookies(), session.headers());
    }

    public Response executeWithoutRetry(Function<APIRequestBuilder, Response> fn) {
        APIRequestBuilder req = newRequest();
        try {
             req = newRequest();
            return fn.apply(req);
        } catch (AssertionError e) {
            Loggers.logError("\nCurrent Exception is: " + e.getMessage());
            Loggers.logError("\nThe response received is: ");
            Loggers.logError(req.getLastResponseLog());
            Loggers.logError("\nThe request sent is: ");
            Loggers.logError(req.getLastRequestLog());
            throw e;
        }
    }


    public Response executeWithRetry(Function<APIRequestBuilder, Response> fn, int expectedStatusCode) {
        APIRequestBuilder req = newRequest();
        Response res = null;
            res = fn.apply(req);
            if (res.getStatusCode() != expectedStatusCode) {
                Loggers.logInfo("Failed first request. refreshing session and trying again");
                tokenProvider.refreshSession();
                req = newRequest();
                res = fn.apply(req);
                if(res.getStatusCode() != expectedStatusCode){
                    Loggers.logError("\nThe response received is: ");
                    Loggers.logError(req.getLastResponseLog());
                    Loggers.logError("\nThe request sent is: ");
                    Loggers.logError(req.getLastRequestLog());
                }
            }
        return res;
    }

    public Response executeWithRetry(Function<APIRequestBuilder, Response> fn, int expectedStatusCode,Boolean assertion) {
        APIRequestBuilder req = newRequest();
        Response res = null;
        try {
            res = fn.apply(req);
            if (res.getStatusCode() != expectedStatusCode) {
                Loggers.logInfo("Failed first request. refreshing session and trying again");
                tokenProvider.refreshSession();
                req = newRequest();
                res = fn.apply(req);
            }
            ResponseActions.checkResponseStatus(res, expectedStatusCode);
        } catch (AssertionError e) {
            Loggers.logError("\nCurrent Exception is: " + e.getMessage());
            Loggers.logError("\nThe response received is: ");
            assert res != null;
            Loggers.logError(req.getLastResponseLog());
            Loggers.logError("\nThe request sent is: ");
            Loggers.logError(req.getLastRequestLog());
            throw e;
        }
        return res;
    }
}
