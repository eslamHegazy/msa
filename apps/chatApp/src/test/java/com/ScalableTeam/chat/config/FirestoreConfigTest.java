package com.ScalableTeam.chat.config;

import com.ScalableTeam.chat.app.config.ConfigData;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@TestConfiguration
public class FirestoreConfigTest {

    @Bean
    public static Firestore connectDBTest() {
        try {
            // Use a service account
            InputStream serviceAccount = Files.newInputStream(Paths.get(ConfigData.firestoreURL));
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
