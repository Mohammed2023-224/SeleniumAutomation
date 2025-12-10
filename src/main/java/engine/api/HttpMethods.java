package engine.api;

public enum HttpMethods {
    POST ("post")  ,
    GET ("get"),
    PUT ("put"),
    PATCH ("patch"),
    DELETE  ("delete");

    private final String method;
    public String getMethod() {
        return method;
    }
    HttpMethods(String method) {
        this.method = method;
    }
}
