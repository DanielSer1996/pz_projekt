package i5b5.daniel.serszen.pz.model.exceptions;

import i5b5.daniel.serszen.pz.model.exceptions.codes.LoginExceptionCodes;

public class LoginException extends Exception{
    private LoginExceptionCodes code;

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginException(String message, LoginExceptionCodes code) {
        super(message);
        this.code = code;
    }

    public LoginExceptionCodes getCode() {
        return code;
    }

    public void setCode(LoginExceptionCodes code) {
        this.code = code;
    }
}
