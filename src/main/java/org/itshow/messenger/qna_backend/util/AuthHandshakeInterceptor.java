package org.itshow.messenger.qna_backend.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final FirebaseAuth firebaseAuth;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");
            try {
                FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);

                UserDto user = new UserDto();
                user.setUserid(decodedToken.getUid());
                user.setEmail(decodedToken.getEmail());
                user.setName(decodedToken.getName());
                user.setImgurl(decodedToken.getPicture());

                attributes.put("user", user);
                return true;
            } catch (FirebaseAuthException e) {
                System.out.println("Firebase 인증 실패: " + e.getMessage());
            }
        }

        System.out.println("WebSocket 연결 거부 - Authorization 헤더 누락");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
