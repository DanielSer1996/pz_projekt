package i5b5.daniel.serszen.pz.model.exceptions;

import i5b5.daniel.serszen.pz.model.exceptions.codes.ResourceExceptionCodes;

public class ResourceException extends Exception{
    private ResourceExceptionCodes code;
    public ResourceException() {
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(String message, ResourceExceptionCodes code, Throwable cause){
        super(message,cause);
        this.code = code;
    }

    public ResourceExceptionCodes getCode() {
        return code;
    }

    public void setCode(ResourceExceptionCodes code) {
        this.code = code;
    }
}
