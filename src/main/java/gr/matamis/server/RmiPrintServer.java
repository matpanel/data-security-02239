package gr.matamis.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiPrintServer extends UnicastRemoteObject implements PrintServer {

    private final ConsolePrintServer consolePrintServer;

    protected RmiPrintServer(AuthenticationService authenticationService) throws RemoteException {
        this.consolePrintServer = new ConsolePrintServer(authenticationService);
    }

    @Override
    public void print(String filename, String printer, Credentials credentials) throws AuthenticationException, PermissionException {
        this.consolePrintServer.print(filename, printer, credentials);
    }

    @Override
    public boolean topQueue(String printer, int job, Credentials credentials) throws AuthenticationException, PermissionException {
        return this.consolePrintServer.topQueue(printer, job, credentials);
    }

    @Override
    public String status(String printer, Credentials credentials) throws AuthenticationException, PermissionException {
        return this.consolePrintServer.status(printer, credentials);
    }

    @Override
    public List<String> queue(String printer, Credentials credentials) throws AuthenticationException, PermissionException {
        return this.consolePrintServer.queue(printer, credentials);
    }

    @Override
    public void start(Credentials credentials) throws AuthenticationException, PermissionException {
        this.consolePrintServer.start(credentials);
    }

    @Override
    public void restart(Credentials credentials) throws AuthenticationException, PermissionException {
        this.consolePrintServer.restart(credentials);
    }

    @Override
    public void stop(Credentials credentials) throws AuthenticationException, PermissionException {
        this.consolePrintServer.stop(credentials);
    }

    @Override
    public void readConfig(String parameter, Credentials credentials) throws AuthenticationException, PermissionException {
        this.consolePrintServer.readConfig(parameter, credentials);
    }

    @Override
    public void setConfig(String parameter, String value, Credentials credentials) throws AuthenticationException, PermissionException {
        this.consolePrintServer.setConfig(parameter, value, credentials);
    }
}
