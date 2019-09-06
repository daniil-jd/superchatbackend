package ru.itpark.exception;

public class ChatRoomAlreadyExistException  extends DefaultException {
    public ChatRoomAlreadyExistException(String message) {
        super(message);
    }
    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
