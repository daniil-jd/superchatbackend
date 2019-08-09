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
import ru.itpark.dto.chat.room.MemberDto;
import ru.itpark.exception.RoomNotFindException;
import ru.itpark.service.RoomsService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WSHandler implements WebSocketHandler {
    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper mapper;
    private final RoomsService roomsService;

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
        System.out.println(webSocketMessage);
        // проверяем первую команду
        // если не устраивает, то close()
        // webSocketSession.close();

        if (webSocketMessage instanceof TextMessage) {
            // так можем делать mapping
             var messageFromSocket = mapper.readValue(((TextMessage) webSocketMessage).getPayload(), MessageDto.class);
             var room = roomsService.findRoomByName(messageFromSocket.getRoomName());
             if (!room.isPresent()) {
                 throw new RoomNotFindException();
             }
            var roomMembersDto = room.get().getMembersDto();

            // схема для аутентификации
            // Websocket -> HTTP GET (Upgrade) -> Cookies
            // System.out.println(webSocketSession.getPrincipal());
            // можно первой командой получать аутентификационные данные
            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
                for (MemberDto member : roomMembersDto) {
                    if (entry.getValue().getPrincipal().getName().equals(member.getUsername())) {
                        entry.getValue().sendMessage(new TextMessage(((TextMessage) webSocketMessage).getPayload()));
                    }
                }

//                if (webSocketSession.getPrincipal().equals(entry.getValue().getPrincipal())) {
//                    entry.getValue().sendMessage(new TextMessage(((TextMessage) webSocketMessage).getPayload()));
//                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    // При закрытии: если была ошибка то error, потом close, если не было то просто close
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        System.out.println(closeStatus.getReason());
        System.out.println(closeStatus.getCode());
        sessions.remove(webSocketSession.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
//    return false;
        return true; // см. last = true
    }
}
