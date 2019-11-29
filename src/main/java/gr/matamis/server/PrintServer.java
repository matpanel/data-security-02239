package gr.matamis.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PrintServer extends Remote {

    static String ACL_PRINT = "print";
    static String ACL_TOP_QUEUE = "topQueue";
    static String ACL_STATUS = "status";
    static String ACL_QUEUE = "queue";
    static String ACL_START = "start";
    static String ACL_RESTART = "restart";
    static String ACL_STOP = "stop";
    static String ACL_READ_CONFIG = "readConfig";
    static String ACL_SET_CONFIG = "setConfig";

    void print(String filename, String printer, Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    boolean topQueue(String printer, int job, Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    String status(String printer, Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    List<String> queue(String printer, Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    void start(Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    void restart(Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    void stop(Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    void readConfig(String parameter, Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;

    void setConfig(String parameter, String value, Credentials credentials) throws AuthenticationException, RemoteException, PermissionException;


}
