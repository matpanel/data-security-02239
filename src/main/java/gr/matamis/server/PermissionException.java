package gr.matamis.server;

public class PermissionException extends Exception {
    private final Credentials credentials;
    private final String functionName;

    @Override
    public String getMessage() {
        return "user " + credentials.getUsername() + " does not have the permission for "+ functionName;
    }

    public PermissionException(Credentials credentials, String functionName) {
        this.credentials = credentials;
        this.functionName = functionName;
    }
}
