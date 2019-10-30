package gr.matamis.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import gr.matamis.server.AuthenticationException;
import gr.matamis.server.Credentials;
import gr.matamis.server.PrintServer;

public class PrinterClient {

    public static void main(String[] args) {


        try {
            String filename = args[0];
            String printerName = args[1];
            String username = args[2];
            String password = args[3];
            Remote remoteComponent = Naming.lookup("rmi://localhost:5050/printer");

            PrintServer printServer = (PrintServer) remoteComponent;
            Credentials panosCredentials = new Credentials(username, password);
//            Credentials jonasCredentials = new Credentials("jonas", "DTU#02239dtu");

            List<Credentials> credentialsList = new ArrayList<>();
            credentialsList.add(panosCredentials);
 //           credentialsList.add(jonasCredentials);
            for (Credentials userCredentials : credentialsList) {
                try {
                    printServer.print(filename, printerName, userCredentials);
                    System.out.println("Everything Ok for " + userCredentials.getUsername());
                } catch (AuthenticationException ae) {
                    System.err.println("Oups: Not authenticated. " + ae.getMessage());
                }
            }


            try {
                printServer.start(panosCredentials);
            } catch (AuthenticationException ae) {
                System.err.println("Oups: Not authenticated. " + ae.getMessage());
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.err.println("Access forbidden from the server: " + e.getMessage());

        }

        System.out.println("Job Done!");
    }

}
