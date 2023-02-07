package examples.auction;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;

/**
 * The server mainline that generates and registers an instance of
 * AuctioneerImpl.

 * <p>Usage: java AuctioneerMainline registryport serverport item-for-sale

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class AuctioneerMainline {
    public static void main(String args[]) {
		if (args.length < 3) {
			System.err.println( "Usage:\njava AuctioneerMainline <registryport> <serverport> <item-for-sale>" ) ;
			return;
		}

		try {
			String hostname = (InetAddress.getLocalHost()).getCanonicalHostName() ;
			int registryport = Integer.parseInt(args[0]) ;
			int serverport = Integer.parseInt(args[1]) ;

			// Get the item for sale from the command-line arguments.
			String item = args[2];

			AuctioneerImpl serv = new AuctioneerImpl(item);
			AuctioneerInterface stub = (AuctioneerInterface) UnicastRemoteObject.exportObject(serv, serverport);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry(registryport);
			registry.rebind("rmi://" + hostname + ":" + registryport + "/Auctioneer", stub);
			// registry.rebind("au", stub);

			System.err.println("Server ready");
			System.err.println("hostname: " + hostname);
		}
		catch (java.net.UnknownHostException e) {
			System.err.println("It seems that Java can't determine the local host!");
		}
		catch (java.io.IOException e) {
			System.out.println("Failed to register.");
		}
    }
}
