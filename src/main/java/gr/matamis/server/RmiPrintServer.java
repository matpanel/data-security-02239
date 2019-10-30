package gr.matamis.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiPrintServer extends UnicastRemoteObject implements PrintServer {

    private final ConsolePrintServer consolePrintServer;

    protected RmiPrintServer(AuthenticationService authenticationService) throws RemoteException {
        this.consolePrintServer = new ConsolePrintServer(authenticationService);
    }

    @Override
    public void print(String filename, String printer, Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.print(filename, printer, credentials);
    }

    @Override
    public void queue(Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.queue(credentials);
    }

    @Override
    public void start(Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.start(credentials);
    }

    @Override
    public void restart(Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.restart(credentials);
    }

    @Override
    public void stop(Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.stop(credentials);
    }

    @Override
    public void printerStatus(Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.printerStatus(credentials);
    }

    @Override
    public void readConfig(String parameter, Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.readConfig(parameter, credentials);
    }

    @Override
    public void setConfig(String parameter, String value, Credentials credentials) throws AuthenticationException {
        this.consolePrintServer.setConfig(parameter, value, credentials);
    }
}
