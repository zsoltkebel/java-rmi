package examples.sps;

import java.io.Serializable;

/**
 * A serializable data structure that encapsulates the choices made by
 * both players of the scissors-paper-stone game and provides an
 * assessment of the outcome of the game.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class SPS implements Serializable {
    private char cc, sc; // cc = client choice, sc = server choice
    private boolean _clientWon = false;
    private boolean _serverWon = false;
    private boolean _draw = false;

    public SPS(char cm) {
		cc = cm ;
    }
    
    /**
     * This method enables the server to make its move in the game.
     */

    public SPS serverMove(char sm) {
	sc = sm;
	switch (sc) {
	case 'c':
	    switch (cc) {
	    case 'p':
		_serverWon = true;
		break;
	    case 's':
		_clientWon = true;
		break;
	    default:
		_draw = true;
	    }
	    break;
	case 'p':
	    switch (cc) {
	    case 's':
		_serverWon = true;
		break;
	    case 'c':
		_clientWon = true;
		break;
	    default:
		_draw = true;
	    }
	    break;
	case 's':
	    switch (cc) {
	    case 'c':
		_serverWon = true;
		break;
	    case 'p':
		_clientWon = true;
		break;
	    default:
		_draw = true;
	    }
	}
	return this;
    }

    /**
     * Returns true if the client has won.
     */

    public boolean clientWon()
    {
	return _clientWon;
    }

    /**
     * Returns true if the server has won.
     */

    public boolean serverWon()
    {
	return _serverWon;
    }

    /**
     * Returns true if there is a draw.

     * <p>There is a draw in the SPS game if both client and server
     * have made the same choice.
     */

    public boolean draw()
    {
	return _draw;
    }

    /**
     * Returns a String representation of the instance.
     */

    public String toString()
    {
	return "You chose = " + reportChoice(cc) +
	    "\nYour opponent chose = " + reportChoice(sc);
    }

    private String reportChoice( char c )
    {
	switch (c) {
	case 'c':
	    return "Scissors";
	case 'p':
	    return "Paper";
	case 's':
	    return "Stone";
	default:
	    return "Invalid choice";
	}
    }
}
