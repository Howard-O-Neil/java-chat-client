package com.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes(App.MESSAGE_SERVICE_PREFIX);
    registry.enableSimpleBroker("/room", "/conversation");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // server: http://larryjason.com

    registry
      .addEndpoint(App.MESSAGE_SERVICE_SOCKET)
      .setAllowedOrigins("http://larryjason.com", "http://www.larryjason.com")
      .withSockJS();
  }
}
