package gr.matamis.server;

public class FileBasedAuthenticator implements AuthenticationService {
    @Override
    public boolean isAuthenticated(Credentials credentials) {
        return false;
    }
}
