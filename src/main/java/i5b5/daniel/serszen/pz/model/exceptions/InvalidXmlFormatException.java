package i5b5.daniel.serszen.pz.model.exceptions;

public class InvalidXmlFormatException extends Exception{
    public InvalidXmlFormatException() {
    }

    public InvalidXmlFormatException(String message) {
        super(message);
    }

    public InvalidXmlFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
