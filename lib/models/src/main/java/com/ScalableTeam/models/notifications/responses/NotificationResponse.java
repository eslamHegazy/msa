package com.ScalableTeam.models.notifications.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@ToString
@Getter
@AllArgsConstructor
public class NotificationResponse {

    private String id;
    private String title;
    private String body;
    private String sender;
    private Date timestamp;
    private boolean isRead;
}
