package org.itshow.messenger.qna_backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException{
        InputStream serviceAccount =
                new ClassPathResource("firebase/serviceAccountKey.json").getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("qna-backend.appspot.com")
                .build();

        // 이미 초기화된 FirebaseApp 있는 경우 중복 초기화 방지
        if(FirebaseApp.getApps().isEmpty()){
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }

    // FirebaseAuth Bean - 인증 처리
    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp){
        return FirebaseAuth.getInstance(firebaseApp);
    }

    // Firebase Messaging Bean - 푸시 메시지
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp){
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    // StorageClient Bean - Firebase Storage
    @Bean
    public StorageClient storageClient(FirebaseApp firebaseApp){
        return StorageClient.getInstance(firebaseApp);
    }
}