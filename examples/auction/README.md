# Challenge

1. Build and run the Auction example and understand how it works.

2. Adapt the bidder client and the auctioneer so that the client can find out what the item is, and then let the user know this information before prompting for a bid. The client should also allow the user to decide not to bid.

3. Adapt the bidder client and the auctioneer so that the client can find out what the current highest bid is (it is no longer a sealed bid auction).

4. Adapt the auctioneer code so that the auction terminates after a specified number of minutes rather than when a sufficient number of bids have been received. This duration (specified as one of the command-line arguments to AuctioneerMainline) should be passed to the constructor of the AuctioneerImpl class in milliseconds. The auctioneer should then close the auction after that period of time and inform the bidders of their success/failure. The bidder should also be told how long the auction has to run before they submit a bid. The auction is now more like those on eBay. This task is more challenging than the first two, but to give you a hint to a solution, the code below is a simple thread that sleeps for a number of milliseconds and calls method callbackToBidders() when it wakes up: