package com.ScalableTeam.notifications.data;

import com.ScalableTeam.notifications.exceptions.FirebaseCredentialsException;
import com.ScalableTeam.notifications.exceptions.FirebaseNotificationException;
import com.google.firebase.messaging.*;

import java.io.IOException;
import java.util.List;

public class NotificationEventHandler {

    private static NotificationEventHandler instance = null;

    private final FirebaseMessaging firebaseMessaging;

    private NotificationEventHandler() throws IOException {
        this.firebaseMessaging = FirebaseMessaging.getInstance();
    }

    public static NotificationEventHandler getInstance() throws FirebaseCredentialsException {
        if (instance == null) {
            NotificationEventHandler handler;
            try {
                handler = new NotificationEventHandler();
            } catch (IOException e) {
                throw new FirebaseCredentialsException("Could not load Firebase credentials.", e);
            }
            instance = handler;
        }
        return instance;
    }

    private Notification createNotification(String title, String body) {
        return Notification.builder().setTitle(title).setBody(body).build();
    }

    private String notifySingleToken(String deviceToken, Notification notification) throws FirebaseNotificationException {
        final Message message = Message.builder().setNotification(notification).setToken(deviceToken).build();
        final String response;

        try {
            response = firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new FirebaseNotificationException("Notification failed to send.", e);
        }

        return "Notification sent successfully. Response: " + response;
    }

    private String notifyMultipleTokens(List<String> deviceTokens, Notification notification) throws FirebaseNotificationException {
        final MulticastMessage message = MulticastMessage.builder().setNotification(notification).addAllTokens(deviceTokens).build();

        final BatchResponse response;

        try {
            response = firebaseMessaging.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new FirebaseNotificationException("Notification failed to send.", e);
        }

        return "Notification sent successfully to " + response.getSuccessCount() + " out of " + deviceTokens.size() + ". Response: " + response;
    }

    public String notify(List<String> deviceTokens, String title, String body) throws FirebaseNotificationException {
        final Notification notification = createNotification(title, body);

        if (deviceTokens.isEmpty()) {
            throw new FirebaseNotificationException("Notification failed to send. Tokens are empty.", null);
        }

        if (deviceTokens.size() == 1) {
            return notifySingleToken(deviceTokens.get(0), notification);
        }

        return notifyMultipleTokens(deviceTokens, notification);
    }
}
