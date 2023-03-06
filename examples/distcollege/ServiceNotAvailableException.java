package examples.distcollege;

/**
 * An exception class that indicates that the database access service
 * is not available.
 * 
 * <p>
 * The use of this exception class avoids the need to throw
 * SQLExceptions to clients (something we want to avoid).
 */

public class ServiceNotAvailableException extends Exception {

    public ServiceNotAvailableException() {
        super();
    }

    public ServiceNotAvailableException(String msg) {
        super(msg);
    }
}
