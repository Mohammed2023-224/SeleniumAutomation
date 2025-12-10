package engine.api;

public interface TokenProvider {
    AuthSession getSession();
    AuthSession refreshSession();

}
