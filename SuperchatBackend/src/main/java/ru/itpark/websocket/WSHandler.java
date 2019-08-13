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
import ru.itpark.service.MessageService;
import ru.itpark.service.RoomsService;
import ru.itpark.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
        System.out.println("connected");
        sessions.put(webSocketSession.getId(), webSocketSession);
        System.out.println(sessions.size());
    }

    // Text, Binary
    // Jackson -> marshalling/unmarshalling
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof TextMessage) {
            var messageFromSocket = mapper.readValue(((TextMessage) webSocketMessage).getPayload(), MessageDto.class);
            var roomUsers = roomsService.findUsersInRoomByRoomName(messageFromSocket.getRoomName());
            if (messageFromSocket.getStatus().equals("MESSAGE")) {
                var messageEntity = new MessageEntity(
                        messageFromSocket.getId(),
                        roomsService.findByRoomName(messageFromSocket.getRoomName()),
                        userService.findUserEntityByName(messageFromSocket.getAuthorName()),
                        messageFromSocket.getMessage(),
                        OffsetDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(messageFromSocket.getCreated())), ZoneId.of("UTC")),
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
            } else if (messageFromSocket.getStatus().equals("GET_ALL_CHAT_MESSAGES")) {
                var messages = messageService.getAllMessagesByRoom(roomsService.findByRoomName(messageFromSocket.getRoomName()));

                if (messages.size() == 0) {
                    messages.add(new MessageDto(0, "system", messageFromSocket.getRoomName(), "No messages yet", Timestamp.valueOf(LocalDateTime.now()).toString(), "SYSTEM"));
                }

                webSocketSession.sendMessage(new TextMessage( mapper.writeValueAsString(messages) ));
            }

            // схема для аутентификации
            // Websocket -> HTTP GET (Upgrade) -> Cookies
            // System.out.println(webSocketSession.getPrincipal());
            // можно первой командой получать аутентификационные данные
//            for (Map.Entry<String, WebSocketSession> sessionEntry : sessions.entrySet()) {
//                for (UserEntity user : roomUsers) {
//                    if (sessionEntry.getValue().getPrincipal().getName().equals(user.getUsername())) {
//                        sessionEntry.getValue().sendMessage(new TextMessage(((TextMessage) webSocketMessage).getPayload()));
//                    }
//                }
//            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    // При закрытии: если была ошибка то error, потом close, если не было то просто close
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println("close socket " + closeStatus.getReason());
        System.out.println(closeStatus.getCode());
        sessions.remove(webSocketSession.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
//    return false;
        return true; // см. last = true
    }
}
