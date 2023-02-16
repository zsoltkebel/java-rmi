package examples.factory;

import java.net.InetAddress;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The server mainline for the connection factory.

 * <p> There are two tasks to be performed here:
 * <ol>
 * <li> Generate a connection factory remote object instance.
 * <li> Register its remote reference with the rmiregistry.
 * </ol>

 * <p> Of course, we need to obtain and check the integrity of the
 * run-time parameters and set up a security manager.

 * The run-time parameters are:
 * <ul>
 * <li> args[0] = the port at which the rmiregistry is listening.
 * <li> args[1] = the port on which to export objects.
 * <li> args[2] = the minimum time for a connection to sleep().
 * <li> args[3] = the maximum time for a connection to sleep().

 * @see ConnectionFactoryInterface
 * @see ConnectionFactoryImpl

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class FactoryMainline {
    /**
     * Generate the factory object and register it with the rmiregistry.
     */
    public static void main(String args[]) {
		if (args.length < 4) {
			System.err.println( "Usage:\njava cs3515.examples.factory.FactoryMainline <registryport> <serverport> <minDelay> <maxDelay>" ) ;
			return;
		}

		try {
			String hostname = (InetAddress.getLocalHost()).getCanonicalHostName();
			int registryport = Integer.parseInt( args[0] );
			int serverport = Integer.parseInt( args[1] );
			int minDelay = Integer.parseInt( args[2] );
			int maxDelay = Integer.parseInt( args[3] );
		
			if (minDelay < 0) {
				System.err.println( "The minimum delay must be a positive integer." );
				return;
			}
			if (maxDelay < minDelay) {
				System.err.println( "The maximum delay must be greater than or equal to the minimum delay." );
				return;
			}
		

			ConnectionFactoryImpl serv = new ConnectionFactoryImpl(serverport, minDelay, maxDelay);
			ConnectionFactoryInterface stub = (ConnectionFactoryInterface) UnicastRemoteObject.exportObject(serv, serverport);

			Registry registry = LocateRegistry.getRegistry(registryport);
			registry.rebind( "rmi://" + hostname + ":" + registryport + "/Factory", stub );
		}
		catch(java.net.UnknownHostException e) {
			System.err.println( e.getMessage() );
		}
		catch (java.io.IOException e) {
			System.err.println( e.getMessage() );
		}
    }
}
