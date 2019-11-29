package gr.matamis.server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrintApplicationServer {

    private static final String HASHED_PASSWORD_FILE = "shadow.data";
    private static final String ROLES_FILE = "roles.data";
    private static final String USER_ROLES_FILE = "user_roles.data";
    private static final String DEFAULT_MODE = "acl";

    public static void main(String[] args) {

        final String mode = args.length > 0 ? args[0].toLowerCase() : DEFAULT_MODE;

        try {
            Path passwordFile = Paths.get(HASHED_PASSWORD_FILE);

            AuthenticationService authenticationService = null;

            switch (mode) {
                case "rbac":
                    Path rolesFile = Paths.get(ROLES_FILE);
                    Path userRolesFile = Paths.get(USER_ROLES_FILE);
                    System.out.println("Using RBAC mode");
                    authenticationService = new HashedFileBasedRBACAuthenticator(passwordFile, rolesFile, userRolesFile);
                    break;

                case "acl":
                    System.out.println("Using ACL mode");
                    authenticationService = new HashedFileBasedAuthenticator(passwordFile);
                    break;

                default:
                    System.err.println("Unknown mode "+ mode);
                    System.exit(1);
                    break;
            }

            Remote printServer = new RmiPrintServer(authenticationService);

            Registry registry = LocateRegistry.createRegistry(5050);
            registry.rebind("printer", printServer);
        } catch (IOException e) {
            System.err.println("Access Denied: " + e.getMessage());
        }
    }
}
