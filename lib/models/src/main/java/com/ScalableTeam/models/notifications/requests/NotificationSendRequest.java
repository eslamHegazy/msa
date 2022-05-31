package com.ScalableTeam.models.notifications.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSendRequest {

    private String title;
    private String body;
    private String sender;
    private List<String> receivers;
}
