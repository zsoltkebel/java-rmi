# Factory

Originally developed by Tim Norman, refactored and updated by Zsolt Kebel at the University of Aberdeen.

## Run example

1. Build the example from the root directory \
`make factory`

2. Start the rmiregistry so that it listens on one of the ports allocated to you. \
`rmiregistry XXXXX`

3. Run the server mainline on the same host as the rmiregistry.
The server will register the funnel manager remote object with the registry,
so you need to tell it the port at which the registry is listening and the port at which to export remote objects. \
`java examples.factory.FactoryMainline <XXXXX> <YYYYY> <minDelay> <maxDelay>`

4. Now start clients in the following way… \
`java cs3524.examples.factory.ClientSimulator localhost XXXXX 30 10 50`

## Important concepts

- References to remote objects may be passed as normal parameters. In this application, references to objects that implement ConnectionInterface are passed to the client in response to a getConnection() call. It is also important to note that this is a “pass by reference” (to the remote object) and not a pass by value as in object serialization, which we learned about in the scissors-paper-stone example.

- The persistence of object references. The object reference for the funnel manager remote object is sufficiently permanent that it is worth registering it with the rmiregistry. This means that many clients may obtain a reference and interact with the remote object; in fact, the application is designed specifically to manage the fact that multiple clients may wish to connect to the resource it is representing at a time. In contrast, Connection objects do not have such persistence; they are specifically designed to provide access to the managed resource for a single session between the client and that resource. In distributed object systems, these are often referred to as complying with the session pattern.

- The type returned by the getConnection() method is ConnectionInterface (i.e. the interface type) and not the specific implementation of the interface (in this case ConnectionImpl). This underlines the fact that the client deals only with the external persona of the remote object, whose character is specified in this interface.

- The final important issue to note is the way the client is designed. The client program generates a number of threads which operate as clients to the server to test out the operation of the factory. Each client thread that obtains a ConnectionInterface interacts with that connection independently. So, the obvious question to ask is why we don’t need to explicitly generate threads in the same way on the server side? In fact, each connection to the manager is handled by a seperate thread, but these threads are generated and managed by the RMI infrastructure allowing the programmer to focus on the logic of the server. This infrastructure is the server-side proxy, or skeleton, which interfaces with the RMIP (RMI Protocol) layer that sits on top of TCP (Transmission Control Protocol).

## Challenge

Limiting the number of Connections
The problem to be solved in this practical is to extend the system to limit the number of connections that can be open at any one time.

> **Hint.** Add an additional parameter to the FactoryMainline indicating the maximum number of open connections, which is passed to the constructor of ConnectionFactoryMainline, then modify getConnection() to do the appropriate test (note that the client handles a null returned from getConnection() - see ClientThread.run())..