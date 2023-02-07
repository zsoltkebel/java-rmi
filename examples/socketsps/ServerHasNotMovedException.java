package examples.socketsps;

/**
 * An exception class, instances of which will be thrown if the result
 * of the game is queried, but the server has not made its choice.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ServerHasNotMovedException extends Exception {

    public ServerHasNotMovedException() {
	    super() ;
    }

    public ServerHasNotMovedException(String msg) {
	    super( msg ) ;
    }
}
