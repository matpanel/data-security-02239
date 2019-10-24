package gr.matamis.server;

public class AuthenticationException extends Exception {
    private final Credentials credentials;

    @Override
    public String getMessage() {
        return "Wrong password for user: " + credentials.getUsername();
    }

    public AuthenticationException(Credentials credentials) {
        this.credentials = credentials;
    }
}
