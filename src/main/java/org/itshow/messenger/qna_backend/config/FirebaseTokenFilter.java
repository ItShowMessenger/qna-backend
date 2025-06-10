package org.itshow.messenger.qna_backend.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.itshow.messenger.qna_backend.dto.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {
    private final FirebaseAuth firebaseAuth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if(token == null){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Firebase 토큰 검증
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);

            UserDto userDto = new UserDto();
            userDto.setEmail(decodedToken.getEmail());
            userDto.setName(decodedToken.getName());
            userDto.setImgurl(decodedToken.getPicture());

            // SecurityContext에 인증 정보 저장
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }catch (FirebaseAuthException e){
            logger.warn("Firebase 인증 실패: " + e.getMessage());
        }catch (Exception e){
            logger.error("에러", e);
        }

        filterChain.doFilter(request, response);
    }
    private String extractToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(StringUtils.hasText(header) && header.startsWith("Bearer ")){
            return header.replace("Bearer ", "");
        }
        return null;
    }
}
