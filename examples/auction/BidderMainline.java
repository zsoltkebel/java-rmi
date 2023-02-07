package examples.auction;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The server mainline that generates and registers an instance of
 * BidderImpl for callbacks and then submits a bid to the auctioneer.

 * <p>Usage: java BidderMainline registryhost registryport callbackport bid

 * <p>where the rmiregistry at which the auctioneer remote object is
 * registered is listening at the communication endpoint 
 * registryhost:registryport and the callback object is exported to callbackport

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class BidderMainline {
    public static void main(String args[]) {
		if (args.length < 4) {
			System.err.println("Usage:\njava BidderMainline <registryhost> <registryport> <callbackport> <bid>") ;
			return;
		}

		try {
			String hostname = args[0];
			int registryport = Integer.parseInt(args[1]) ;
			int callbackport = Integer.parseInt(args[2]) ;

			// Get the price being bid from the command-line arguments.
			float price = Float.parseFloat( args[3] );

			BidderImpl bidder = new BidderImpl();
			BidderInterface bidderstub = (BidderInterface) UnicastRemoteObject.exportObject(bidder, callbackport);

			String regURL = "rmi://" + hostname + ":" + registryport + "/Auctioneer";
			
			// Look up remote object
			Registry registry = LocateRegistry.getRegistry(registryport);
			AuctioneerInterface auc = (AuctioneerInterface) registry.lookup(regURL);

			auc.bid(bidderstub, price);
		}
		catch(java.rmi.NotBoundException e) {
			System.err.println( "Can't find the auctioneer in the registry." );
		}
		catch (java.io.IOException e) {
			System.out.println( "Failed to register." );
		}
	}
}
