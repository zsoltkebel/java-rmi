package examples.socketsps;

/**
 * An exception class, instances of which will be thrown if the server
 * attempts to override its original choice made in an SPS instance.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ServerHasAlreadyMovedException extends Exception {

    public ServerHasAlreadyMovedException() {
	    super() ;
    }

    public ServerHasAlreadyMovedException(String msg) {
	    super( msg ) ;
    }
}
