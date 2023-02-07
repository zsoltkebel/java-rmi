package examples.rmishout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A client that invokes the SHOUT service provided.
 * <p>Usage: java ShoutClient host port
 * <p>where host:port represents the connection endpoint at which the
 * rmiregistry on the server's host is listening.
 * @author Tim Norman, University of Aberdeen
 * @version 2.0
 */

public class ShoutClient {
    public static void main(String args[]) throws RemoteException {
		if (args.length < 2) {
			System.err.println( "Usage:\njava ShoutClient <host> <port>" ) ;
			return;
		}

		String hostname = args[0];
		int port = Integer.parseInt( args[1] );

		try {
			// Obtain the server handle from the RMI registry
			// listening at hostname:port.
			String regURL = "rmi://" + hostname + ":" + port + "/ShoutService";
			System.out.println( "Looking up " + regURL );

			// Look up remote object
			Registry registry = LocateRegistry.getRegistry(port);
			ShoutServerInterface serv = (ShoutServerInterface) registry.lookup(regURL);
			
			// Prompt the user for a message to send to the server.
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			System.out.println( "Enter message below:" );

			// Invoke the server and print out the result.
			System.out.println(serv.shout(in.readLine()));
		} catch (java.io.IOException e) {
			System.err.println( "I/O error." );
			System.err.println( e.getMessage() );
		} catch (java.rmi.NotBoundException e) {
			System.err.println( "Server not bound." );
			System.err.println( e.getMessage() );
		}
	}
}
