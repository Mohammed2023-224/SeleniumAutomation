package engine.api;

import java.util.Map;

public class NoAuthTokenProvider implements TokenProvider{
    private static final AuthSession EMPTY_SESSION = new AuthSession(
            Map.of(),      // no headers
            Map.of(),      // no cookies
            Long.MAX_VALUE // never expires
    );

    @Override
    public AuthSession getSession() {
        return EMPTY_SESSION;
    }

    @Override
    public AuthSession refreshSession() {
        return EMPTY_SESSION;
    }
}
