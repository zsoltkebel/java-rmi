package examples.auction;

import java.rmi.RemoteException;
import java.util.Vector;
import java.util.Enumeration;


/**
 * An implementation of the AuctioneerInterface remote interface.

 * @see AuctioneerInterface

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */
public class AuctioneerImpl implements AuctioneerInterface {
    private String item;
    private int numBidsRequired = 3;
    private int numBidsReceived = 0;
    private float maxBid = -1;

    private BidderInterface winner = null;
    private Vector<BidderInterface> loosers = new Vector<BidderInterface>();

    /**
     * The constructor.  All that is required is to record the item
     * for sale.

     * @param item The item for sale at auction.
     */
    public AuctioneerImpl(String item) throws RemoteException {
		this.item = item;
    }

    /**
     * Method to handle a bid submitted for the item being auctioned.

     * This method is synchronized so that we ensure that the
     * auctioneer handles one bid at a time.

     * @param bidder A reference to the bidder submitting this bid for
     * call-back.
     * @param price The price bid for the item on sale.
     */
    public synchronized void bid(BidderInterface bidder, float price) throws RemoteException {
		numBidsReceived += 1;

		System.out.println( "New bid received = " + price );

		// If the new bid is equal to the current maximum bid, there
		// is a clash and both of these bidders become loosers.
		if (price == maxBid) {

			System.out.println( "The new bid is the same as the current top bid." );

			loosers.add(winner);
			loosers.add(bidder);
			winner = null;
		}
		// If we have received a new top bid, replace the old top bid
		// with the new one, and record the new winner.
		if (price > maxBid) {

			System.out.println( "The new bid is better than the current top bid." );

			if (winner != null)
			loosers.add( winner );
			winner = bidder;
			maxBid = price;
		}
		// If the new bid received is lower that the current top bid,
		// this bidder is a looser.
		if (price < maxBid) {

			System.out.println( "The new bid is worse than the current top bid." );

			loosers.add( bidder );
		}

		// Once we have received sufficient bids, call back to the
		// bidders with the result of the auction.
		if (numBidsReceived >= numBidsRequired)
			notifyBidders();
	}

	/**
	 * Calls back to all bidders to inform them of the outcome of the
	 * auction.
	 */
	private void notifyBidders() {
		try {
			String msg = "Item not sold.";
			// There may not be a winner, so we need to check that the
			// winner is not null.
			if (winner != null) {
				winner.won(item, maxBid);
				msg = "The item was sold for " + maxBid + ".";
			}

			// Let all the others know that they have lost and inform them
			// of the outcome of the auction.
			for (Enumeration<BidderInterface> e = loosers.elements() ; e.hasMoreElements() ;) {
				BidderInterface b = (BidderInterface) e.nextElement();
				if (b != null)
					b.lost(item, msg);
			}

			System.out.println( "Clearing up references to bidders and resetting the auction..." );

			numBidsReceived = 0;
			maxBid = -1;
			winner = null;
			loosers.removeAllElements();

			System.out.println( "...done.  Please wait while the Java VM garbage collects.\n" +
					"Once this is done, the bidders should shut down.\n" +
								"Of course, you can force shutdown using ^C.\n" +
								"---------------------------------------------------------\n" +
								"Now, three new bidders can be run. " );
		} catch (RemoteException re) {
			System.err.println( "Failure to contact callback remote object." );
			re.printStackTrace( System.err );
		}
	}
}

