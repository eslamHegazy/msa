package com.ScalableTeam.notifications.data;

import com.ScalableTeam.notifications.models.requests.DeviceTokenRequest;
import com.ScalableTeam.notifications.models.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.models.requests.NotificationReadRequest;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;

import java.util.Date;
import java.util.List;

public class FakeData {

    public static final String USER_ID_1 = "user1_1234567890";
    public static final String USER_ID_2 = "user2_1234567890";
    public static final String DEVICE_TOKEN_1 = "token1_1234567890";
    public static final String DEVICE_TOKEN_2 = "token2_1234567890";
    public static final String NOTIFICATION_ID = "notification_1234567890";

    public static final NotificationSendRequest NOTIFICATION_SEND_REQUEST = new NotificationSendRequest("Title", "Body", USER_ID_1, List.of(USER_ID_2));
    public static final NotificationReadRequest NOTIFICATION_READ_REQUEST = new NotificationReadRequest(USER_ID_2, NOTIFICATION_ID);
    public static final NotificationDeleteRequest NOTIFICATION_DELETE_REQUEST = new NotificationDeleteRequest(USER_ID_2, NOTIFICATION_ID);
    public static final DeviceTokenRequest DEVICE_TOKEN_REQUEST_1 = new DeviceTokenRequest(USER_ID_1, DEVICE_TOKEN_1);
    public static final DeviceTokenRequest DEVICE_TOKEN_REQUEST_2 = new DeviceTokenRequest(USER_ID_2, DEVICE_TOKEN_2);

    public static final NotificationResponse NOTIFICATION_RESPONSE = new NotificationResponse(NOTIFICATION_ID, "Title", "Body", USER_ID_2, new Date(), false);
}
