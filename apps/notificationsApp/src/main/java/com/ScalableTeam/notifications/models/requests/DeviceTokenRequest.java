package com.ScalableTeam.notifications.models.requests;

import lombok.Data;

@Data
public class DeviceTokenRequest {

    private final String userId;
    private final String deviceToken;

    public DeviceTokenRequest(String userId, String deviceToken) {
        this.userId = userId;
        this.deviceToken = deviceToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
