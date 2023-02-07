/*******************************************************************
 * cs3515.examples.socketsps.Stone                                 *
 *******************************************************************/

package examples.socketsps;

/**
 * Represents the Stone choice in the scissors-paper-stone game.

 * The class implements the Serializable interface because it is a
 * subclass of the abstract class Choice.

 * This specification overrides methods win(), lose() and toString()
 * declared in the abstract superclass.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class Stone extends Choice {
    /**
     * Indicates whether or not Stone wins over the choice passed
     * to it as a parameter: stone blunts scissors.
     */
    public boolean win(Choice o) {
	    return (o instanceof Scissors);
    }

    /**
     * Indicates whether or not Stone looses over the choice passed
     * to it as a parameter: paper wraps stone.
     */
    public boolean lose(Choice o) {
	    return (o instanceof Paper);
    }

    /**
     * Used to convert the class to a String for presentation
     * purposes.
     */
    public String toString() {
	    return "Stone";
    }
}
