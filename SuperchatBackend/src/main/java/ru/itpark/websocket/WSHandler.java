package ru.itpark.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.itpark.dto.chat.message.MessageDto;
import ru.itpark.entity.UserEntity;
import ru.itpark.entity.chat.MessageEntity;
import ru.itpark.entity.chat.MessageStatus;
import ru.itpark.exception.WebSocketException;
import ru.itpark.service.MessageService;
import ru.itpark.service.RoomsService;
import ru.itpark.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WSHandler implements WebSocketHandler {
    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;
    private final RoomsService roomsService;
    private final MessageService messageService;
    private final UserService userService;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        sessions.put(webSocketSession.getId(), webSocketSession);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {
            var messageFromSocket = mapper.readValue(((TextMessage) webSocketMessage).getPayload(), MessageDto.class);
            var roomUsers = roomsService.findUsersInRoomByRoomName(messageFromSocket.getRoomName());
            if (messageFromSocket.getStatus().equalsIgnoreCase(MessageStatus.MESSAGE.name())) {
                var messageEntity = new MessageEntity(
                        messageFromSocket.getId(),
                        roomsService.findByRoomName(messageFromSocket.getRoomName()),
                        userService.findUserEntityByName(messageFromSocket.getAuthorName()),
                        messageFromSocket.getMessage(),
                        Timestamp.from(Instant.ofEpochMilli((messageFromSocket.getCreated()))),
                        MessageStatus.MESSAGE
                );
                messageService.save(messageEntity);
                var messages = messageService.getAllMessagesByRoom(roomsService.findByRoomName(messageFromSocket.getRoomName()));

                for (Map.Entry<String, WebSocketSession> sessionEntry : sessions.entrySet()) {
                    for (UserEntity user : roomUsers) {
                        if (sessionEntry.getValue().getPrincipal().getName().equals(user.getUsername())) {
                            sessionEntry.getValue().sendMessage(new TextMessage( mapper.writeValueAsString(messages) ));
                        }
                    }
                }
            } else if (messageFromSocket.getStatus().equalsIgnoreCase(MessageStatus.GET_ALL_CHAT_MESSAGES.name())) {
                var messages = messageService.getAllMessagesByRoom(roomsService.findByRoomName(messageFromSocket.getRoomName()));

                if (messages.size() == 0) {
                    messages.add(new MessageDto(0, "system", messageFromSocket.getRoomName(), "No messages yet", Timestamp.valueOf(LocalDateTime.now()).getTime(), MessageStatus.GET_ALL_CHAT_MESSAGES.name()));
                }

                webSocketSession.sendMessage(new TextMessage( mapper.writeValueAsString(messages) ));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        throw new WebSocketException(throwable.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        if (closeStatus.equals(CloseStatus.SERVER_ERROR)) {
            throw new WebSocketException(closeStatus.getReason());
        }

        sessions.remove(webSocketSession.getId());
        webSocketSession.close(closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}
