package gr.matamis.server;

import java.rmi.RemoteException;

public class ConsolePrintServer implements PrintServer {
    private final AuthenticationService authenticationService;

    public ConsolePrintServer(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void print(String filename, String printer, Credentials credentials) throws AuthenticationException {

        if (authenticationService.isAuthenticated(credentials)) {
            System.out.println("Printing " + filename + " on printer " + printer + " for user " + credentials.getUsername());
        } else {
            throw new AuthenticationException(credentials);
        }
    }

    @Override
    public void queue(Credentials credentials) throws RemoteException {

    }

    @Override
    public void start(Credentials credentials) throws RemoteException {

    }

    @Override
    public void restart(Credentials credentials) throws RemoteException {

    }

    @Override
    public void stop(Credentials credentials) throws RemoteException {

    }

    @Override
    public void printerStatus(Credentials credentials) throws RemoteException {
    }

    @Override
    public void readConfig(String parameter, Credentials credentials) throws RemoteException {

    }

    @Override
    public void setConfig(String parameter, String value, Credentials credentials) throws RemoteException {

    }
}
