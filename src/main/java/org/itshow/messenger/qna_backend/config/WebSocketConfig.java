package org.itshow.messenger.qna_backend.config;

import com.google.firebase.auth.FirebaseAuth;
import org.itshow.messenger.qna_backend.util.AuthHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final FirebaseAuth firebaseAuth;
    public WebSocketConfig(FirebaseAuth firebaseAuth){
        this.firebaseAuth = firebaseAuth;
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")    // WebSocket 연결 주소
                .addInterceptors(new AuthHandshakeInterceptor(firebaseAuth))
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue");  // 메시지 수신 구독
        registry.setApplicationDestinationPrefixes("/app"); // 메시지 전송
    }
}
