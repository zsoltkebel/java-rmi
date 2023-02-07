/*******************************************************************
 * cs3515.examples.socketsps.SPSClient                             *
 *******************************************************************/

package examples.socketsps;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.Socket;

/**
 * A client that enables the user to play a number of instances of the
 * scissors-paper-stone game.

 * <p>Usage: java SPSClient host port

 * <p>where host:port represents the connection endpoint at which the
 * SPSServer is listening.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class SPSClient
{
	public static void main(String args[]) {
		if (args.length < 2) {
			System.err.println( "Usage:\njava SPSClient <host> <port>" );
			return;
		}

		String hostname = args[0];
		int port = Integer.parseInt( args[1] );
		
		try {
			// Obtain an input stream for user input.
			BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
			// Connect to and set up input/output streams with the server
			Socket server = new Socket( hostname, port );
			ObjectOutputStream serverOut = new ObjectOutputStream( server.getOutputStream() );
			ObjectInputStream serverIn = new ObjectInputStream( server.getInputStream() );

			SPS result = null;
			char sel;
			// Prompt the user for a move and iterate until the user
			// no longer wishes to play.
			do {
				System.out.println( "Please enter your move (c=scissors, p=paper, s=stone)\nor any other character to exit." );
				sel = in.readLine().charAt(0);

				switch (sel) {
					case 'c':
						serverOut.writeObject( new SPS( new Scissors() ) );
						serverOut.flush();
						result = (SPS)serverIn.readObject();
						break;
					case 'p':
						serverOut.writeObject( new SPS( new Paper() ) );
						serverOut.flush();
						result = (SPS)serverIn.readObject();
						break;
					case 's':
						serverOut.writeObject( new SPS( new Stone() ) );
						serverOut.flush();
						result = (SPS)serverIn.readObject();
						break;
					default:
						server.close();
						System.out.println( "Goodbye" );
						result = null;
				}
				// Report the outcome of the game to the user.
				if (result != null) {
					System.out.println( result.toString() );

					if (result.clientWon())
						System.out.println( "Congratulations, you won!" );
					if (result.serverWon())
						System.out.println( "Commiserations, you lost!" );
					if (result.draw())
						System.out.println( "A draw.  Try again." );
				}
			} while ( result != null );
		} 
		catch (ServerHasNotMovedException e) {
			System.err.println( "The server did not make a move!" );
		}
		catch (StringIndexOutOfBoundsException e) {
			System.out.println( "Goodbye" );
		}
		catch (Exception e) {
			e.printStackTrace( System.err );
		}
	}
}
