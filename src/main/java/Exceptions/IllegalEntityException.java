package Exceptions;

/**
 * Created by matusmacko on 27.3.17.
 */
public class IllegalEntityException extends RuntimeException {
    public IllegalEntityException() {
        super();
    }

    public IllegalEntityException(String msg) {
        super(msg);
    }

    public IllegalEntityException(String msg, Exception ex) {
        super(msg, ex);
    }
}
