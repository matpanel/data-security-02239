package gr.matamis.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrintServer extends Remote {

    void print(String filename, String printer, Credentials credentials) throws AuthenticationException, RemoteException;

    void topQueue(String printer, int job, Credentials credentials) throws AuthenticationException, RemoteException;

    String status(String printer, Credentials credentials) throws AuthenticationException, RemoteException;

    Integer queue(String printer, Credentials credentials) throws AuthenticationException, RemoteException;

    void start(Credentials credentials) throws AuthenticationException, RemoteException;

    void restart(Credentials credentials) throws AuthenticationException, RemoteException;

    void stop(Credentials credentials) throws AuthenticationException, RemoteException;

    void readConfig(String parameter, Credentials credentials) throws AuthenticationException, RemoteException;

    void setConfig(String parameter, String value, Credentials credentials) throws AuthenticationException, RemoteException;


}
