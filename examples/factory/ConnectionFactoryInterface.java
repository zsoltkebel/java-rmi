package examples.factory;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote persona of a ConnectionFactory object.

 * <p> Any implementation of this interface must implement at least
 * these methods.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public interface ConnectionFactoryInterface extends Remote {
    /**
     * Attempt to obtain a connection to the remote resource.
     */
    public ConnectionInterface getConnection() throws RemoteException;

    /**
     * Release connection.  Should only be called by the connection
     * object.
     */
    public void releaseConnection( String id ) throws RemoteException;
}
