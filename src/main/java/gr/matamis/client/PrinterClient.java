package gr.matamis.client;

import gr.matamis.server.AuthenticationException;
import gr.matamis.server.Credentials;
import gr.matamis.server.PrintServer;

import java.awt.*;
import java.awt.geom.Area;
import java.io.Serializable;
import java.net.CacheRequest;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PrinterClient {

    public static void main(String[] args) {


        try {
            String filename = args[0];
            String printerName = args[1];
            Remote remoteComponent = Naming.lookup("rmi://localhost:5050/printer");

            PrintServer printServer = (PrintServer) remoteComponent;
            Credentials panosCredentials = new Credentials("panos", "sonap");
            Credentials jonasCredentials = new Credentials("jonas", "123345");

            List<Credentials> credentialsList = new ArrayList<>();
            credentialsList.add(panosCredentials);
            credentialsList.add(jonasCredentials);
            for (Credentials userCredentials : credentialsList) {
                try {
                    printServer.print(filename, printerName, userCredentials);
                    System.out.println("Everything Ok for " + userCredentials.getUsername());
                } catch (AuthenticationException ae) {
                    System.err.println("Ton mpoulo: Not authenticated. Error response was: " + ae.getMessage());
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.err.println("Ton mpoulo apo server: " + e.getMessage());

        }

        System.out.println("Job Done!");
    }

}
