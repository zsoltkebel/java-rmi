package examples.factory;

import java.rmi.RemoteException;

/**
 * Each instance represents a single client.
 
 * This thread obtains a reference to the connection factory
 * manager.  When run, the thread will attempt to obtain a
 * connection, tells it to doWork and then releases the connection.

 * These threads are generated by the FunnelClientSimulator.

 * @see ClientSimulator

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ClientThread extends Thread {
    private int id;
    private ConnectionFactoryInterface man;
    
    /**
     * Create a new client simulator thread.
     
     * @param id An integer identifying this thread used for display purposes.
     * @param man A reference to the remote object of type Manager.
     */
    public ClientThread(int id, ConnectionFactoryInterface man) {
		this.id = id;
		this.man = man;
    }

    /**
     * This thread attempts to obtain a connection from the funnel
     * manager.  When a connection is obtained, it obtains the
     * connection's identifier, calls the sleep() method and releases
     * the connection.
     */
    public void run() {
		try {
			ConnectionInterface con = man.getConnection();
			if (con != null) {
				String cid = con.getID();
				System.out.println( "Client " + id + " has acquired connection with id " + cid );
				con.doWork();
				System.out.println( "Client " + id + " is releasing its connection." );
				con.release();
			} else {
				System.out.println( "Client " + id + " has been refused a connection." );
			}
	    }
		catch (RemoteException e) {
			System.err.println( "ClientThread: id=" + id + " remote exception caught." );
			e.printStackTrace( System.err );
		}
		catch (Exception e){
			e.printStackTrace( System.err );
	    }
    }
}