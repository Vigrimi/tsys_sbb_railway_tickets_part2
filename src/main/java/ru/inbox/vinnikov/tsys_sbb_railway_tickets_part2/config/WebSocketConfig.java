package ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import ru.inbox.vinnikov.tsys_sbb_railway_tickets_part2.service.PongWebSocket;

@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer /*WebSocketMessageBrokerConfigurer*/ {
    @Bean
    public PongWebSocket pongWebSocket(){
        return new PongWebSocket();
    }
//    public PongWebSocket pongWebSocket;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // регистрация вебсокета, чтобы он срабатывал и привязали к адресу в js ws = new WebSocket("ws://localhost:8095/sbb/v2/pong");
        registry.addHandler(pongWebSocket(),"/sbb/v2/pong");
//        registry.addHandler(pongWebSocket,"/sbb/v2/pong");
    }



    /*@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }
dd
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/hello").withSockJS();
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }*/
}
