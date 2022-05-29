package com.ScalableTeam.notifications.data;

import com.ScalableTeam.models.notifications.requests.DeviceTokenRequest;
import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.models.notifications.requests.NotificationReadRequest;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import com.ScalableTeam.models.notifications.responses.NotificationResponse;

import java.util.Date;
import java.util.List;

public class FakeData {

    public static final String USER_ID_1 = "fake_user_1";
    public static final String USER_ID_2 = "fake_user_2";
    public static final String DEVICE_TOKEN_1 = "fake_token_1";
    public static final String DEVICE_TOKEN_2 = "fake_token_2";
    public static final String NOTIFICATION_ID = "fake_notification";

    public static final NotificationSendRequest NOTIFICATION_SEND_REQUEST =
            new NotificationSendRequest("Title", "Body", USER_ID_1, List.of(USER_ID_2));

    public static final NotificationReadRequest NOTIFICATION_READ_REQUEST =
            new NotificationReadRequest(USER_ID_2, NOTIFICATION_ID);

    public static final NotificationDeleteRequest NOTIFICATION_DELETE_REQUEST =
            new NotificationDeleteRequest(USER_ID_2, NOTIFICATION_ID);

    public static final DeviceTokenRequest REGISTER_DEVICE_TOKEN_REQUEST =
            new DeviceTokenRequest(USER_ID_2, DEVICE_TOKEN_1);

    public static final DeviceTokenRequest UNREGISTER_DEVICE_TOKEN_REQUEST =
            new DeviceTokenRequest(USER_ID_2, DEVICE_TOKEN_2);

    public static final NotificationResponse NOTIFICATION_RESPONSE =
            new NotificationResponse(NOTIFICATION_ID, "FakeTitle", "FakeBody", USER_ID_2, new Date(), false);
}
