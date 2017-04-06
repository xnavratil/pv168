package Exceptions;

/**
 * Created by pnavratil on 4/4/17.
 */
public class MissionException extends Exception {

    public MissionException(String message) {
        super(message);
    }

    public MissionException(String message, Throwable cause) {
        super(message, cause);
    }

}