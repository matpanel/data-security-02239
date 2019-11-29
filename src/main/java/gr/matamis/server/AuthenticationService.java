package gr.matamis.server;

public interface AuthenticationService {

    boolean isAuthenticated(Credentials credentials);

    boolean hasPermission(Credentials credentials, String functionName);
}
