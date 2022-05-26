package com.ScalableTeam.chat.config;

import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class FirestoreConfig {

    @Bean
    public static Firestore connectDB() {
        try {
            // Use a service account
            InputStream serviceAccount = Files.newInputStream(Paths.get("/media/george/Academic/GUC/Semester 10/MSA/msa/apps/chatApp/src/main/java/com/ScalableTeam/chat/app/config/chat-app-db-service-key.json"));
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);

            return FirestoreClient.getFirestore();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
