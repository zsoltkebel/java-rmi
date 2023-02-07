package examples.socketsps;

import java.io.Serializable;

/**
 * A serializable data structure that encapsulates the choices made by
 * both players of the scissors-paper-stone game and provides an
 * assessment of the outcome of the game.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class SPS implements Serializable {
    private Choice cc, sc; // cc = client-choise,  sc = server-choice

    public SPS(Choice cm) {
	    cc = cm ;
    }
    
    /**
     * This method enables the server to make its move in the game.

     * <p>The method throws a ServerHasAlreadyMovedException in case
     * the server attempts to override its first choice.  This is to
     * prevent the server from trying out all possibilities until it
     * finds one such that serverWon() returns true.
     */

    public SPS serverMove(Choice sm) throws ServerHasAlreadyMovedException {
	    if (sc != null) throw new ServerHasAlreadyMovedException();
	    sc = sm;
	    return this;
    }

    /**
     * Returns true if the client has won.

     * <p>Throws a ServerHasNotMovedException if the server has not
     * yet made its selection.  The sole constructor ensures that the
     * client has made its choice.
     */

    public boolean clientWon() throws ServerHasNotMovedException {
	    if (sc == null) throw new ServerHasNotMovedException();
	    return cc.win(sc) ;
    }

    /**
     * Returns true if the server has won.

     * <p>Throws a ServerHasNotMovedException if the server has not
     * yet made its selection.  The sole constructor ensures that the
     * client has made its choice.
     */

    public boolean serverWon() throws ServerHasNotMovedException {
	    if (sc == null) throw new ServerHasNotMovedException();
	    return sc.win(cc) ;
    }

    /**
     * Returns true if there is a draw.

     * <p>There is a draw in the SPS game if both client and server
     * have made the same choice.

     * <p>Throws a ServerHasNotMovedException if the server has not
     * yet made its selection.  The sole constructor ensures that the
     * client has made its choice.
     */

    public boolean draw() throws ServerHasNotMovedException {
	    if (sc == null) throw new ServerHasNotMovedException();
	    return ( cc.draw(sc) ) ;
    }

    /**
     * Returns a String representation of the instance.
     */

    public String toString() {
	    return "You chose = " + cc.toString() + "\nYour opponent chose = " + sc.toString();
    }
}
