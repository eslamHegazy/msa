package com.ScalableTeam.models.notifications.responses;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationResponse {

    private final String id;
    private final String title;
    private final String body;
    private final String sender;
    private final Date timestamp;
    private final boolean isRead;

    public NotificationResponse(String id, String title, String body, String sender, Date timestamp, boolean isRead) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public String getId() {
        return id;
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

    public boolean isRead() {
        return isRead;
    }
}
