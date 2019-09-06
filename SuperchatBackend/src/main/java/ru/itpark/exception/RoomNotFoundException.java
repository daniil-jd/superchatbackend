package ru.itpark.exception;

public class RoomNotFoundException  extends DefaultException {
    public RoomNotFoundException(String message) {
        super(message);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
