package gr.matamis.client;

import gr.matamis.server.AuthenticationException;
import gr.matamis.server.Credentials;
import gr.matamis.server.PermissionException;
import gr.matamis.server.PrintServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrinterClientConsole {
    public static void main(String[] args) {


        try {
/*
//    case1  Someone can input all arguments at the same time
            String filename = args[0];
            String printerName = args[1];
            String username = args[2];
            String password = args[3];
 */

/*
//  case2 someone can input the arguments from the console
//  has the benefit that it doesn't echo your password to the console
//  sadly this way it cannot be run from inside the IDE.

            Console console = System.console();
            if (console==null){
                System.out.println("No console: not in interactive mode!");
                System.exit(0);
            }
            System.out.print("Enter your username: ");
            String username = console.readLine();

// the password is not revealed at the console
            System.out.println("Enter your password: ");
            char[] passwordchar = console.readPassword();
            String password = String.valueOf(passwordchar);

            System.out.print("Which file would you like to print? Enter filename: ");
            String filename=console.readLine();

            System.out.print("Enter printer name: (hint printer0 or printer1?)");
            String printerName = console.readLine();

 */

//   case3   Taking input using the Scanner Class
            Scanner scn = new Scanner(System.in);
            System.out.println("Enter your username: ");
            String username = scn.nextLine();

            System.out.println("Enter your password: ");
            String password = scn.nextLine();

            System.out.println("Which file would you like to print? Enter filename: ");
            String filename = scn.nextLine();

            System.out.println("Enter printer name: (hint printer0 or printer1)");
            String printerName = scn.nextLine();

            Remote remoteComponent = Naming.lookup("rmi://localhost:5050/printer");

            PrintServer printServer = (PrintServer) remoteComponent;
            Credentials credentials = new Credentials(username, password);

            // the line below was written for testing purposes
            //            Credentials jonasCredentials = new Credentials("jonas", "DTU#02239dtu");

            List<Credentials> credentialsList = new ArrayList<>();
            credentialsList.add(credentials);
            //           credentialsList.add(jonasCredentials);
            for (Credentials userCredentials : credentialsList) {
                try {
                    printServer.print(filename, printerName, userCredentials);
                    System.out.println("Everything Ok for " + userCredentials.getUsername());
                } catch (AuthenticationException ae) {
                    System.err.println("Oups: Not authenticated. " + ae.getMessage());
                }
                catch (PermissionException pe) {
                    System.err.println("Oups: Not having the permission. " + pe.getMessage());
                }
            }


            try {
                printServer.start(credentials);
            } catch (AuthenticationException ae) {
                System.err.println("Oups: Not authenticated. " + ae.getMessage());
            }
            catch (PermissionException pe) {
                System.err.println("Oups: Not having the permission. " + pe.getMessage());
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.err.println("Access forbidden from the server: " + e.getMessage());

        }

        System.out.println("Job Done!");
    }

}
