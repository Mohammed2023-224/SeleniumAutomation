package engine.api;

import java.util.Map;

public class AuthSession {
    private final Map<String, String> headers;
    private final Map<String, String> cookies;
    private final long expiresAtEpochMillis;

    public AuthSession(Map<String, String> headers,
                       Map<String, String> cookies,
                       long expiresAtEpochMillis) {
        this.headers = Map.copyOf(headers);
        this.cookies = Map.copyOf(cookies);
        this.expiresAtEpochMillis = expiresAtEpochMillis;
    }

    public Map<String, String> headers() { return headers; }
    public Map<String, String> cookies() { return cookies; }
    public boolean isExpired() { return System.currentTimeMillis() >= expiresAtEpochMillis; }
}
