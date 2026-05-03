// package com.cewar.game;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.lang.NonNull;
// import org.springframework.messaging.simp.config.MessageBrokerRegistry;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
// import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
// import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// @Configuration
// @EnableWebSocketMessageBroker // enables WebSocket message handling
// public class GameWebSocketConfig implements WebSocketMessageBrokerConfigurer {

//     /**
//      * Implements default method in {@link WebSocketMessageBrokerConfigurer}
//      */
//     @Override
//     public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
//         config.enableSimpleBroker("/game");
//         config.setApplicationDestinationPrefixes("/app");
//     }

//     @Override
//     public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
//         registry.addEndpoint("/ce-websocket");
//     }
// }
