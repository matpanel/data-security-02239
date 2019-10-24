package gr.matamis.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrintApplicationServer {

    public static void main(String[] args) {
        try {
//            AuthenticationService authenticationService = new MirrorAuthenticator();
            AuthenticationService authenticationService = new FileBasedAuthenticator();
            Remote printServer = new RmiPrintServer(authenticationService);

            Registry registry = LocateRegistry.createRegistry(5050);
            registry.rebind("printer", printServer);
        } catch (RemoteException e) {
            System.err.println("Ton mpoulo: " + e.getMessage());
        }
    }
}
