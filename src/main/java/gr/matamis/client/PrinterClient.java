package gr.matamis.client;

import java.io.Console;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import gr.matamis.server.AuthenticationException;
import gr.matamis.server.Credentials;
import gr.matamis.server.PrintServer;

public class PrinterClient {

    public static void main(String[] args) {


        Remote remoteComponent = null;
        try {
            remoteComponent = Naming.lookup("rmi://localhost:5050/printer");


            PrintServer printServer = (PrintServer) remoteComponent;


            Scanner scn = new Scanner(System.in);
            System.out.println("Enter your username: ");
            String username = scn.nextLine();

            System.out.println("Enter your password: ");
            String password = scn.nextLine();

            Credentials credentials = new Credentials(username, password);

            String printerName;
            String configProperty;

            boolean terminate = false;

            do {
                int selectedOption = selectOption();
                switch (selectedOption) {


                    case 1:
                        System.out.println("Which file would you like to print? Enter filename: ");
                        String filename = scn.nextLine();

                        System.out.println("Enter printer name: (hint printer0 or printer1)");
                        printerName = scn.nextLine();

                        printServer.print(filename, printerName, credentials);

                        break;
                    case 2:
                        System.out.println("Enter printer name: (hint printer0 or printer1)");
                        printerName = scn.nextLine();
                        List<String> queue = printServer.queue(printerName, credentials);
                        if (queue != null) {
                            System.out.println("This is the queue for " + printerName);
                            for (String jobEntry : queue) {
                                System.out.println(jobEntry);
                            }
                            System.out.println();
                        } else {
                            System.out.println("Queue size of printer " + printerName + " not found");
                            System.out.println();
                        }
                        break;
                    case 3:

                        System.out.println("Select printer name: (hint printer0 or printer1)");
                        printerName = scn.nextLine();

                        System.out.println("Which job would you like to move to the top of " + printerName + "? Enter job number:");
                        int jobNumber = scn.nextInt();
                        if (printServer.topQueue(printerName, jobNumber, credentials)) {
                            System.out.println("Successfully moved job with id " + jobNumber + "to the top of " + printerName + "queue");
                        } else {
                            System.err.println("Failed to move job with id " + jobNumber + "to the top of " + printerName + "queue");
                        }

                        break;
                    case 4:
                        printServer.start(credentials);
                        break;
                    case 5:
                        printServer.stop(credentials);
                        break;
                    case 6:
                        printServer.restart(credentials);
                        break;
                    case 7:
                        System.out.println("Select printer name: (hint printer0 or printer1)");
                        printerName = scn.nextLine();
                        String result = printServer.status(printerName, credentials);
                        if (result != null) {
                            System.out.println("Status of printer " + printerName + " is " + result);
                            System.out.println();
                        } else {
                            System.out.println("Status for " + printerName + " not found");
                            System.out.println();
                        }

                        break;
                    case 8:
                        System.out.println("Select config property: ");
                        configProperty = scn.nextLine();
                        printServer.readConfig(configProperty, credentials);

                        break;
                    case 9:
                        System.out.println("Select config property: ");
                        configProperty = scn.nextLine();
                        System.out.println("Enter config value: ");
                        String configValue = scn.nextLine();
                        printServer.setConfig(configProperty, configValue, credentials);
                        break;
                    case 10:
                        terminate = true;
                        break;
                    default:
                        System.err.println("Unknown option!");
                        break;
                }
            } while (!terminate);


            System.out.println("Job Done!");

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        } catch (AuthenticationException ae) {
            System.err.println("Oups: Failed to authenticate. " + ae.getMessage());
        }
    }

    private static int selectOption() {

        Scanner reader = new Scanner(System.in);
        System.out.println("Select an option : ");
        System.out.println("1.  print filename on specified printer ");
        System.out.println("2.  list the print queue  ");
        System.out.println("3.  move job on top of queue ");
        System.out.println("4.  start the print server  ");
        System.out.println("5.  stop the print server  ");
        System.out.println("6.  restart the print server  ");
        System.out.println("7.  print status of printer on display  ");
        System.out.println("8.  read configuration  ");
        System.out.println("9. change configuration  ");
        System.out.println("10. EXIT  ");

        System.out.println();
        System.out.println("Please select option:");
        return reader.nextInt();
    }

}
