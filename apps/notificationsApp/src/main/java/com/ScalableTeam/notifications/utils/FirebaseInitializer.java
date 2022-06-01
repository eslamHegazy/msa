package com.ScalableTeam.notifications.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FirebaseInitializer {

    private static boolean isInitialized;

    public static boolean initialize() {
        try {
            if (!isInitialized) {
                GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
                FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.getApplicationDefault()).build();
                FirebaseApp.initializeApp(options);
                isInitialized = true;
                System.out.println("Firebase notification app has been initialized.");
            } else {
                System.out.println("Firebase notification app has already been initialized.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
