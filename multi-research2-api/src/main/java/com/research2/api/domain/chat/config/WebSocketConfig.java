package com.research2.api.domain.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // [1] Registering TaskScheduler for Heartbeat Processing
    @Primary
    @Bean(name = "customTaskScheduler")
    public ThreadPoolTaskScheduler messageBrokerTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("wss-heartbeat-thread-");
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic").setHeartbeatValue(new long[]{4000, 4000}).setTaskScheduler(messageBrokerTaskScheduler());
        config.setApplicationDestinationPrefixes("/app");  // Message sent by client prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//       registry.addEndpoint("/ws-chat").setAllowedOrigins("*").withSockJS();
//      Wildcard patterns are allowed + credentials can also be used.
        registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setSendTimeLimit(15 * 1000)
                .setSendBufferSizeLimit(512 * 1024)
                .setMessageSizeLimit(128 * 1024);
    }
}