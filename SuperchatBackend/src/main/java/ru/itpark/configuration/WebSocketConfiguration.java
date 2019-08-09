package ru.itpark.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServerEndpointRegistration;
import ru.itpark.encoderdecoder.MessageDtoEncoderDecoder;
import ru.itpark.websocket.InheritedEndpoint;
import ru.itpark.websocket.WSHandler;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final WSHandler wsHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("config register web socket");
        registry.addHandler(wsHandler, "/api/iwschat").setAllowedOrigins("*");
    }

//  @Bean
//  public ServletServerContainerFactoryBean servletServerContainerFactoryBean() {
//    var container = new ServletServerContainerFactoryBean();
//    container.setMaxTextMessageBufferSize(10 * 1024 * 1024);
//    container.setMaxBinaryMessageBufferSize(10 * 1024 * 1024);
//    return container;
//  }


    @Bean
    public ServerEndpointRegistration serverEndpointRegistration(InheritedEndpoint endpoint) {
        System.out.println("config iwschat");
        var registration = new ServerEndpointRegistration("/apis/iwschat", endpoint);
        registration.setEncoders(List.of(MessageDtoEncoderDecoder.class));
        registration.setDecoders(List.of(MessageDtoEncoderDecoder.class));
        return registration;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        var exporter = new ServerEndpointExporter();
        return exporter;
    }
}
