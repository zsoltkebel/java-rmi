package examples.factory;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote persona of a connection.

 * <p> Any implementation of this interface must implement at least
 * these methods.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public interface ConnectionInterface extends Remote {

    /**
     * Obtain the identifier of this connection.
     */
    public String getID() throws RemoteException;

    /**
     * Tell the connection to sleep.
     */
    public void doWork() throws RemoteException;

    /**
     * Release the connection.
     */
    public void release() throws RemoteException;
}
