package ru.itpark.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import ru.itpark.dto.chat.message.MessageDto;

import javax.websocket.EncodeException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import java.io.IOException;


@Component
@CrossOrigin
public class InheritedEndpoint extends Endpoint { // на каждое подключение создаётся один экземпляр класса
//  @Autowired
//  private DemoService service;


    public InheritedEndpoint() {
        System.out.println("i'm created (inherited)");
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        session.addMessageHandler(MessageDto.class, new MessageHandler.Whole<MessageDto>() {
            @Override
            public void onMessage(MessageDto dto) {
                System.out.println(dto);
                session.getOpenSessions().forEach(o -> {
                    try {
                        o.getBasicRemote().sendObject(dto);
//                        if (dto.getRoom() == 1) {
//                            o.getBasicRemote().sendObject(new MessageDto(101L, "reply to chat 1: " + dto.getMessage(), 1));
//                        } else if (dto.getRoom() == 2) {
//                            o.getBasicRemote().sendObject(new MessageDto(102L, "reply to chat 2: " + dto.getMessage(), 2));
//                        }

                    } catch (IOException | EncodeException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }



    // @OnOpen
    // @OnClose
}
