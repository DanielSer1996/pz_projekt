package i5b5.daniel.serszen.pz.model.exceptions;

public class IncompatibleEventTypeException extends Exception{
    public IncompatibleEventTypeException(String message) {
        super(message);
    }

    public IncompatibleEventTypeException() {
    }

    public IncompatibleEventTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
