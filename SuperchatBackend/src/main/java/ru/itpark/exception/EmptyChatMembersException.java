package ru.itpark.exception;

public class EmptyChatMembersException  extends DefaultException {
    public EmptyChatMembersException(String message) {
        super(message);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
