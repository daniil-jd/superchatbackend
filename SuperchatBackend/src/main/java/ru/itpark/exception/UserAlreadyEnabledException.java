package ru.itpark.exception;

public class UserAlreadyEnabledException  extends DefaultException {
    public UserAlreadyEnabledException(String message) {
        super(message);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
