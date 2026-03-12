package engine.api;

import java.util.Map;

public class AddedHeaderTokenProvider implements TokenProvider{
    private static final AuthSession addedHeaderSession = new AuthSession(
            Map.of(
                    "x-api-key", "01252cee-4854-49b0-8e32-4215ba456977"
            ),
            Map.of(),      // no cookies
            Long.MAX_VALUE // never expires
    );
    @Override
    public AuthSession getSession() {
        return addedHeaderSession;
    }

    @Override
    public AuthSession refreshSession() {
        return addedHeaderSession;
    }
}
