package ru.itpark.exception;

public class SendMimeMailMessageException  extends DefaultException {
    public SendMimeMailMessageException() {
    }

    public SendMimeMailMessageException(String message) {
        super(message);
    }

    public SendMimeMailMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendMimeMailMessageException(Throwable cause) {
        super(cause);
    }

    public SendMimeMailMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getCode() {
        return "AUTHENTICATION_TOKEN_NOT_FOUND";
    }
}
