package org.itshowmessenger.qna.qna_backend.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirebaseTestController {

    @GetMapping("/test/firebase")
    public String testFirebaseInitialization() {
        try {
            FirebaseApp app = FirebaseApp.getInstance();
            FirebaseAuth auth = FirebaseAuth.getInstance(app);
            FirebaseMessaging messaging = FirebaseMessaging.getInstance(app);
            StorageClient storage = StorageClient.getInstance(app);

            return "✅ Firebase 초기화 성공\n" +
                    "App name: " + app.getName() + "\n" +
                    "Auth: " + auth.toString() + "\n" +
                    "Messaging: " + messaging.toString() + "\n" +
                    "Storage: " + storage.toString();
        } catch (IllegalStateException e) {
            return "❌ Firebase 초기화 실패: " + e.getMessage();
        } catch (Exception e) {
            return "❌ 예외 발생: " + e.getMessage();
        }
    }
}