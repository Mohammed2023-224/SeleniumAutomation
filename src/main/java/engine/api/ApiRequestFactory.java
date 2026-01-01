package engine.api;

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
        APIRequestBuilder builder = new APIRequestBuilder(baseUrl, session.cookies(),session.headers());
        builder.setHeaders( session.headers());
        return builder;
    }

    public Response executeWithoutRetry(Function<APIRequestBuilder, Response> fn) {
        APIRequestBuilder req1 = newRequest();
        return fn.apply(req1);
    }


    public Response executeWithRetry(Function<APIRequestBuilder, Response> fn, int expectedStatusCode) {
        APIRequestBuilder req1 = newRequest();
        Response res = fn.apply(req1);
        if (res.getStatusCode() != expectedStatusCode) {
            tokenProvider.refreshSession();
            APIRequestBuilder req2 = newRequest();
            res = fn.apply(req2);
        }
        return res;
    }
}
