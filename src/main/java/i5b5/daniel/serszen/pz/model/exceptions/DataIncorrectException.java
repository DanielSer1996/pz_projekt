package i5b5.daniel.serszen.pz.model.exceptions;

import i5b5.daniel.serszen.pz.model.exceptions.codes.DataIncorrectExceptionCodes;

public class DataIncorrectException extends Exception{
    private DataIncorrectExceptionCodes code;

    public DataIncorrectException(String message, DataIncorrectExceptionCodes code) {
        super(message);
        this.code = code;
    }

    public DataIncorrectException(DataIncorrectExceptionCodes code) {
        this.code = code;
    }

    public DataIncorrectException(String message, Throwable cause, DataIncorrectExceptionCodes code) {
        super(message, cause);
        this.code = code;
    }

    public DataIncorrectExceptionCodes getCode() {
        return code;
    }

    public void setCode(DataIncorrectExceptionCodes code) {
        this.code = code;
    }
}
