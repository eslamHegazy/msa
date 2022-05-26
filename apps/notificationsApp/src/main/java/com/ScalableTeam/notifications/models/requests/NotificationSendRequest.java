package com.ScalableTeam.notifications.models.requests;

import java.util.List;

public class NotificationSendRequest {

    private final String title;
    private final String body;
    private final String sender;
    private final List<String> receivers;

    public NotificationSendRequest(String title, String body, String sender, List<String> receivers) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.receivers = receivers;
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

    public List<String> getReceivers() {
        return receivers;
    }
}
