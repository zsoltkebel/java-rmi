package examples.factory;

import java.rmi.RemoteException;

import java.util.Vector;
import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;

/**
 * Manages a pool of connections.

 * <p> This class simply provides a means to request a connection and 
 * a means to release the connection (called by the generated connection 
 * itself).  When a ConnectionImpl instance is generated, a
 * reference to it is passed back to the client.

 * @see ConnectionInterface
 * @see ConnectionImpl
 * @see ConnectionFactoryInterface
 
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ConnectionFactoryImpl implements ConnectionFactoryInterface {
    private Vector<String> conIDs;
    private int serverport;
    private int minDelay;
    private int maxDelay;

    /**
     * Initialise the vector of connection identifiers.
     */
    public ConnectionFactoryImpl(int serverport, int minDelay, int maxDelay) throws RemoteException {
        this.conIDs = new Vector<String>();
        this.serverport = serverport;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    /**
     * Get an ID using java.rmi.server.UID, record it, generate
     * a ConnectionImpl object and return it to the client.
     */
    public ConnectionInterface getConnection() throws RemoteException {
        String id = (new UID()).toString();
        synchronized(conIDs) {
            conIDs.addElement(id);
        }
        System.out.println( "Connection with ID = " + id + " generated." );
        ConnectionImpl conn = new ConnectionImpl(id, this, minDelay, maxDelay);
        ConnectionInterface stub = (ConnectionInterface) UnicastRemoteObject.exportObject(conn, serverport);
        return stub;
    }

    /**
     * Used by Connection objects to tell the Factory that the
     * client has released the connection.

     * @param id The identifier of the connection to be released.
     */
    public void releaseConnection(String id) throws RemoteException {
	    boolean wasElement;
        synchronized(conIDs) {
            wasElement = conIDs.removeElement(id);
        }
        if (wasElement) {
            System.out.println( "Connection with ID=" + id + " removed from _conIDs." );
        } else {
            System.out.println( "Runtime error: connection with ID=" + id + " does not exist." );
        }
    }
}
