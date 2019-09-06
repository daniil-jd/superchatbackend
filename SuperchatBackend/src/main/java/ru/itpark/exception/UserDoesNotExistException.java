package ru.itpark.exception;

public class UserDoesNotExistException  extends DefaultException {
    public UserDoesNotExistException(String message) {
        super(message);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
