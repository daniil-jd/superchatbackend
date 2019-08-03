package ru.itpark.exception;

public class AuthenticateTokenException extends RuntimeException {

    public AuthenticateTokenException(String msg) {
        super(msg);
    }
}
