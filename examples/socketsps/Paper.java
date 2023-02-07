package examples.socketsps;

/**
 * Represents the Paper choice in the scissors-paper-stone game.

 * The class implements the Serializable interface because it is a
 * subclass of the abstract class Choice.

 * This specification overrides methods win(), lose() and toString()
 * declared in the abstract superclass.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class Paper extends Choice {
    /**
     * Indicates whether or not Paper wins over the choice passed
     * to it as a parameter: paper wraps stone.
     */
    public boolean win(Choice o) {
	    return (o instanceof Stone);
    }

    /**
     * Indicates whether or not Paper looses over the choice passed
     * to it as a parameter: scissors cut paper.
     */
    public boolean lose(Choice o) {
	    return (o instanceof Scissors);
    }

    /**
     * Used to convert the class to a String for presentation
     * purposes.
     */
    public String toString() {
	    return "Paper";
    }
}
