
# Internet-Relay Chat (IRC)

TELNET protocol is needed Teletype Network Protocol (Telnet)
https://formulae.brew.sh/formula/telnet

## Run example

1. Build the example \
`make irc`

2. Start the server, specifying the port on which it is to listen for connection requests: \
`java examples.irc.ChatServer XXXXXX`

3. Start telnet, indicating the host and port that represent the communication endpoint at which it can find the server. \
`telnet <host-where-server-runs> XXXXXX` or\
`telnet localhost XXXXXX`

> Find out your host name by running the command: `hostname`

## Information

This exercise is concerned with the use of Java’s Socket API and the development of a multi-threaded server that uses this API for communication. In this application the data being passed between the clients and the chat server is unstructured strings of characters, not serialized objects. This means that not only can the clients be easily implemented in any language that supports sockets, but you can use a generic client application such as TELNET.


## Challenge

1. Modifying the ChatServer and ReqTokenizer code so that clients can find out who is logged onto the server at any time. The client command for this function should be WHO.

2. If you wish, you can write your own client.