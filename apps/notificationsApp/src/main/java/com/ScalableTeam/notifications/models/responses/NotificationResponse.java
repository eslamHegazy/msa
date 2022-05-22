package com.ScalableTeam.notifications.models.responses;

import java.util.Date;

public class NotificationResponse {

    private final String title;
    private final String body;
    private final String sender;
    private final Date timestamp;

    public NotificationResponse(String title, String body, String sender, Date timestamp) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
