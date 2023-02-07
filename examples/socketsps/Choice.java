package examples.socketsps;

import java.io.Serializable;

/**
 * Abstract class of Scissors, Paper and Stone for generic win(),
 * lose() and draw() method calls.

 * Instances of this class would be meaningless, hence it being
 * declared abstract.  However, I do want to specify certain default
 * behaviour, hence it not being specified as an interface.

 * The class implements the Serializable interface, which means that
 * the compiler will put in place mechansims for instances of this
 * class (and, importantly, its subclasses) to be marshalled and
 * unmarshalled for transmission over the network.

 * Subclasses should override methods win(), lose() and toString(),
 * but there is no need to override draw() as its behaviour shouldn't
 * need to change from this default.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public abstract class Choice implements Serializable {
    /**
     * An empty constructor.
     */
    public Choice() {}

    /**
     * Indicates whether or not this Choice wins over the choice
     * passed to it as a parameter.
     */
    public boolean win(Choice o) {
	    return false;
    }

    /**
     * Indicates whether or not this Choice looses over the choice
     * passed to it as a parameter.
     */
    public boolean lose(Choice o) {
	    return false;
    }

    /**
     * Indicates whether or not this Choice and the choice passed to
     * it as a parameter draw.
     */
    public boolean draw(Choice o) {
	    return (!(win(o) || lose(o))) ;
    }

    /**
     * Used to convert the class to a String for presentation
     * purposes.
     */
    public String toString() {
	    return "";
    }
}
