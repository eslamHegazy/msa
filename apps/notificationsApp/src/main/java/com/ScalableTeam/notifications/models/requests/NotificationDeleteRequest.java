package com.ScalableTeam.notifications.models.requests;

import lombok.Data;

@Data
public class NotificationDeleteRequest {

    private final String userId;
    private final String notificationId;

    public NotificationDeleteRequest(String userId, String notificationId) {
        this.userId = userId;
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getNotificationId() {
        return notificationId;
    }
}
