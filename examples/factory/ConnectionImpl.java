package examples.factory;

import java.rmi.RemoteException;
import java.util.Random;

/**
 * Represents a connection.

 * <p> These remote objects are temporary, session objects, and hence
 * are not registered with the rmiregistry.  A client object requests
 * a connection by calling method getConnection() on a ConnectionFactoryManager
 * remote object.  Once no longer required, the client should call the
 * method release() on this object.

 * <p> In this example, the unique
 * identifier of the connection can be obtained by the client.
 * Normally, this would be kept private between the Connection and the
 * Manager so that release requests can be authenticated.

 * <p> The performance of a task by the scarce resource is simulated
 * by the client calling method doWork().  This simply causes the
 * connection thread to sleep for a number of milliseconds between
 * minDelay and maxDelay (two of the parameters to the constructor of
 * this class.

 * @see ConnectionInterface
 * @see ConnectionFactoryImpl#getConnection()
 * @see ConnectionFactoryImpl#releaseConnection( String )

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ConnectionImpl implements ConnectionInterface {
    private String id;
    private ConnectionFactoryInterface factory;
    private int minDelay;
    private int maxDelay;
    private Random gen;

    /**
     * Initialises member variables and seeds a random number
     * generator with the current time.
     */
    public ConnectionImpl(String id, ConnectionFactoryInterface factory, int minDelay, int maxDelay) throws RemoteException {
        this.id = id;
        this.factory = factory;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        // Seed the random number generator with the current time.
        this.gen = new Random( System.currentTimeMillis() );
    }

    /**
     * Allows the client to obtain the identifier of this connection.
     * In a practical application this should be kept private between
     * the funnel manager and the connection object.
     */
    public String getID() throws RemoteException {
	    return id;
    }

    /**
     * Causes the thread in which this connection is running to sleep
     * for a number of milliseconds.

     * <p> Note that although this remote method is void, the client
     * thread that called this method will suspend until the method
     * returns; RMI is an example of a synchronous communication
     * mechanism.
     */

    public void doWork() throws RemoteException {
        try {
            // Sleep for some time between minDelay and maxDelay.
            float sleepTime = minDelay + ((maxDelay - minDelay) * gen.nextFloat());
            System.out.println( "Connection " + id + " sleeping for " + sleepTime + " milliseconds." );
            Thread.sleep((long) sleepTime); // sleep current thread
        }
        catch (InterruptedException e) {
            System.err.println( "Thread sleep interruption in sleep() method of cs3515.examples.factory.ConnectionImpl." );
            e.printStackTrace( System.err );
        }
    }

    /**
     * Client uses this method to release its connection to the scarce
     * resource.

     * <p> For the factory to operate correctly, it is imperative that
     * the client does not attempt to access the connection after
     * calling this method.  It should allow the reference to the
     * remote object to go out of scope and leave the JRE to garbage
     * collect this instance.
     */
    public void release() throws RemoteException {
	    factory.releaseConnection(id);
    }
}
