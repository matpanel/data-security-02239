package gr.matamis.server;

public class MirrorAuthenticator implements AuthenticationService {
    @Override
    public boolean isAuthenticated(Credentials credentials) {
        StringBuilder sb = new StringBuilder(credentials.getUsername());
        sb.reverse();
        String mirroredUsername = sb.toString();
        String password = credentials.getPassword();
        return password.equals(mirroredUsername);
    }
}
