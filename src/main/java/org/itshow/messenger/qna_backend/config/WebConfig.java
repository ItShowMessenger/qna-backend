package org.itshow.messenger.qna_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**")  // 허용 경로
                .allowedOrigins("http://localhost:3000", "https://qna-messenger.mirim-it-show.site") // 허용 도메인
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);

        registry.addMapping("/ws-chat/**")
                .allowedOrigins("http://localhost:3000", "https://qna-messenger.mirim-it-show.site")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
