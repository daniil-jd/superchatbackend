package ru.itpark.exception;

public class WebSocketException  extends DefaultException {
    public WebSocketException(String message) {
        super(message);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
