package com.ScalableTeam.notifications;

import com.ScalableTeam.notifications.models.requests.DeviceTokenRequest;
import com.ScalableTeam.notifications.models.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.models.requests.NotificationReadRequest;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;

import java.util.Date;
import java.util.List;

public class FakeData {

    public static final String USER_ID = "user1234567890";
    public static final String NOTIFICATION_ID = "notification1234567890";
    public static final String DEVICE_TOKEN = "token1234567890";

    public static final NotificationSendRequest NOTIFICATION_SEND_REQUEST = new NotificationSendRequest("Title", "Body", "Hussein", List.of(USER_ID));
    public static final NotificationReadRequest NOTIFICATION_READ_REQUEST = new NotificationReadRequest(USER_ID, NOTIFICATION_ID);
    public static final NotificationDeleteRequest NOTIFICATION_DELETE_REQUEST = new NotificationDeleteRequest(USER_ID, NOTIFICATION_ID);
    public static final DeviceTokenRequest DEVICE_TOKEN_REQUEST = new DeviceTokenRequest(USER_ID, DEVICE_TOKEN);

    public static final NotificationResponse NOTIFICATION_RESPONSE = new NotificationResponse(NOTIFICATION_ID, "Title", "Body", USER_ID, new Date(), false);
}
