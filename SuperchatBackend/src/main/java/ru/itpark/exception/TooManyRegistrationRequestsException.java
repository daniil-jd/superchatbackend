package ru.itpark.exception;

public class TooManyRegistrationRequestsException  extends DefaultException {
    public TooManyRegistrationRequestsException(String message) {
        super(message);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
