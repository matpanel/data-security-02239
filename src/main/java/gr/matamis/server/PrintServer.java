package gr.matamis.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PrintServer extends Remote {

    void print(String filename, String printer, Credentials credentials) throws AuthenticationException, RemoteException;

    boolean topQueue(String printer, int job, Credentials credentials) throws AuthenticationException, RemoteException;

    String status(String printer, Credentials credentials) throws AuthenticationException, RemoteException;

   List<String> queue(String printer, Credentials credentials) throws AuthenticationException, RemoteException;

    void start(Credentials credentials) throws AuthenticationException, RemoteException;

    void restart(Credentials credentials) throws AuthenticationException, RemoteException;

    void stop(Credentials credentials) throws AuthenticationException, RemoteException;

    void readConfig(String parameter, Credentials credentials) throws AuthenticationException, RemoteException;

    void setConfig(String parameter, String value, Credentials credentials) throws AuthenticationException, RemoteException;


}
