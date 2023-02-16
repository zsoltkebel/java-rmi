Reworked examples with exercises from the original code of Tim Norton and Felipe Meneguzzi.

Official docs:

https://docs.oracle.com/javase/tutorial/rmi/
https://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html

# Exmaples
## [RMI (Remote Method Invokation) Shout](/examples/rmishout/) (Practical 1)

Instructions to build:

3 separate terminal windows:
1. `make rmishout`
2. Run the Server code ports between 50010 - 50019 \
`rmiregistry XXXXX` \
3. Run the Server \
`java examples.rmishout.ShoutServerMainline XXXXXX YYYYYY` \
3. Run the Client code \
`java examples.rmishout.ShoutClient <yourmachine-name> XXXXXX` \
You can use the command `hostname` to identify your host name.

## [SPS](/examples/sps/) (Practical 1)



## [Socket SPS (Scissors-Paper-Stone)](/examples/socketsps/) (Practical 2)

1. Build with \
`make socketsps`
2. Open a terminal (Server) at reposity root and run \
`java examples.socketsps.SPSServer XXXXXX`
3. Open another terminal (Client) at reposity root and run \
`java examples.socketsps.SPSClient localhost XXXXXX`

## [Auction](/examples/auction/) (Practical 2)

1. `make auction`
2. `rmiregistry XXXXX`
3. New Terminal \
`java examples.auction.AuctioneerMainline XXXXX 50014 laptop`
4. New Termial \
`java examples.auction.BidderMainline oa-edu-188-66.wireless.abdn.ac.uk XXXXX 50016 30`

## [Factory](/examples/factory/)

An example project generating connections to a server.