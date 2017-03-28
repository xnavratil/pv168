package Exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException() {
        super();
    }

    public ValidationException(String msg) {
         super(msg);
    }

    public ValidationException(String msg, Exception ex) {
        super(msg, ex);
    }
}
