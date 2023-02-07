/*******************************************************************
 * cs3515.examples.rmishout.ShoutServerImpl                        *
 *******************************************************************/

package examples.rmishout;

import java.rmi.RemoteException;

/**
 * Converts messages to uppercase.
 * @see ShoutServerInterface
 * @author Tim Norman, University of Aberdeen
 * @version 2.0
 */

public class ShoutServerImpl
    implements ShoutServerInterface
{
    /**
     * The constructor.  Nothing to be done here.
     */
    public ShoutServerImpl() throws RemoteException {}
    
    /**
     * Convert the message to uppercase.
     */
    public String shout(String s) throws RemoteException {
        return s.toUpperCase();
    }
}
