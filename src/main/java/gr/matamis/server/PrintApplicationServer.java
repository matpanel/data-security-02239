package gr.matamis.server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrintApplicationServer {

    private static final String HASHED_PASSWORD_FILE = "shadow.data";

    public static void main(String[] args) {
        try {
            Path passwordFile = Paths.get(HASHED_PASSWORD_FILE);
            AuthenticationService authenticationService = new HashedFileBasedAuthenticator(passwordFile);
            Remote printServer = new RmiPrintServer(authenticationService);

            Registry registry = LocateRegistry.createRegistry(5050);
            registry.rebind("printer", printServer);
        } catch (IOException e) {
            System.err.println("Access Denied: " + e.getMessage());
        }
    }
}
