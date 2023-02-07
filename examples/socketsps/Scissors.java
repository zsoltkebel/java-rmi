package examples.socketsps;

/**
 * Represents the Scissors choice in the scissors-paper-stone game.

 * The class implements the Serializable interface because it is a
 * subclass of the abstract class Choice.

 * This specification overrides methods win(), lose() and toString()
 * declared in the abstract superclass.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class Scissors extends Choice {
    /**
     * Indicates whether or not Scissors wins over the choice passed
     * to it as a parameter: scissors cut paper.
     */
    public boolean win(Choice o) {
	    return (o instanceof Paper);
    }

    /**
     * Indicates whether or not Scissors looses over the choice passed
     * to it as a parameter: stone blunts scissors.
     */
    public boolean lose(Choice o) {
	    return (o instanceof Stone);
    }

    /**
     * Used to convert the class to a String for presentation
     * purposes.
     */
    public String toString() {
	    return "Scissors";
    }
}
