package examples.rmishout;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.net.InetAddress;

/**
 * The server mainline that generates and registers an instance of
 * ShoutServerImpl.
 * <p>Usage: java ShoutServerMainline port
 * <p>where there is an rmiregistry listening at the port specified on
 * the host that this server is running.
 * @author Tim Norman, University of Aberdeen
 * @version 2.0
 */

public class ShoutServerMainline
{
    public static void main(String args[]) {

	if (args.length < 2) {
	    System.err.println( "Usage:\njava ShoutServerMainline <registryport> <serverport>" ) ;
	    return;
	}

	try {
	    String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ;
	    int registryport = Integer.parseInt( args[0] ) ;
	    int serverport = Integer.parseInt( args[1] );

	    // Generate the remote object(s) that will reside on this server.
        ShoutServerImpl shoutserv = new ShoutServerImpl();
	    ShoutServerInterface shoutstub = (ShoutServerInterface) UnicastRemoteObject.exportObject(shoutserv, serverport);

	    String regURL = "rmi://" + hostname + ":" + registryport + "/ShoutService";
        System.out.println("Registering " + regURL );
		// Bind the remote object's stub in the registry
		Registry registry = LocateRegistry.getRegistry(registryport);
		registry.rebind(regURL, shoutstub);

	    // Note the server will not shut down!
		System.err.println("Server ready.");
	}
	catch(java.net.UnknownHostException e) {
	    System.err.println( "Cannot get local host name." );
	    System.err.println( e.getMessage() );
	}
	catch (java.io.IOException e) {
            System.err.println( "Failed to register." );
	    System.err.println( e.getMessage() );
        }
    }
}
