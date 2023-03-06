package examples.distcollege;

/**
 * An exception class that indicates that the course does not exist.
 */

public class NoSuchCourseException extends Exception {

    public NoSuchCourseException() {
        super();
    }

    public NoSuchCourseException(String msg) {
        super(msg);
    }
}
