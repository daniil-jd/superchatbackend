package ru.itpark.exception;

public class ChatRoomAlreadyExist extends RuntimeException {
    public ChatRoomAlreadyExist(String message) {
        super(message);
    }
}
