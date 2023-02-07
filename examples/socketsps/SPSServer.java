/*******************************************************************
 * cs3515.examples.socketsps.SPSServer                             *
 *******************************************************************/

package examples.socketsps;

import java.util.Random;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Provides a random choice between Scissors, Paper and Stone,
 * returning the resulting SPS object to the client.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class SPSServer {
    Random _gen;

    private Socket _clientSocket;
    private ObjectInputStream _clientIn;
    private ObjectOutputStream _clientOut;
	

    /**
     * Check that the port has been specified, and set things rolling.
     */
    public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: java SPSServer <port>");
			return;
		}

		new SPSServer().startListening(Integer.parseInt(args[0]));
	}

    /**
     * Intialise the random number generator and wait for connection
     * requests.
     */
    void startListening(int port) throws IOException {
		_gen = new Random( System.currentTimeMillis() );
		ServerSocket listener = new ServerSocket(port);
		
		System.err.println("Server ready.");

		while (true) {
			_clientSocket = listener.accept();
			
			// Open I/O steams
			_clientIn = new ObjectInputStream( _clientSocket.getInputStream() );
			_clientOut = new ObjectOutputStream( _clientSocket.getOutputStream() );
			go();

			listener.close();
		}
    }

    /**
     * Makes the server's move in the game.
     
     * <p>serverMove() may throw a ServerHasAlreadyMovedException,
     * which must be caught and handled.

     * <p>Termination of the connection is done by the client, and
     * this manifests itself as an IOException here on the server (the
     * connection has been closed by the client), so if this occurs,
     * this method terminates and the server waits for a new
     * connection request.
     */
    void go() {
		try {
			Object cc;
			while (true) {
				cc = _clientIn.readObject();
				if (cc instanceof SPS) {
					_clientOut.writeObject( ((SPS)cc).serverMove( myChoice() ) );
					_clientOut.flush();
				}
				else {
					_clientOut.writeObject( null );
					_clientOut.flush();
				}
			}
		}
		catch (ServerHasAlreadyMovedException e) {
			System.err.println( "ServerAlreadyMovedException caught." );
		}
		catch (ClassNotFoundException e) {
			System.err.println( "Can't find the class of the instance sent to the server." );
			e.printStackTrace( System.err );
		}		
		catch (IOException e) {
			// An IOException is thown when the client closes the
			// connection, so do nothing here.
		}
	}
		
    /**
     * Implements the server's move using the pseudo-random number
     * generator.
     */
    private Choice myChoice() {
		int sel = _gen.nextInt( 3 );
		Choice theChoice = null ;
		switch (sel) {
			case 0:
				theChoice = new Scissors();
				break;
			case 1:
				theChoice = new Paper();
				break;
			default:
				theChoice = new Stone();
			}
		return theChoice;
    }
}
