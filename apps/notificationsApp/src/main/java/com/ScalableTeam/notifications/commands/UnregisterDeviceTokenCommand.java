package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.Command;
import com.ScalableTeam.notifications.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.DeviceTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnregisterDeviceTokenCommand implements Command {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(Object body) throws Exception {
        DeviceTokenRequest deviceToken = (DeviceTokenRequest) body;
        notificationsRepository.unregisterDeviceToken(deviceToken);
        return 200;
    }
}
