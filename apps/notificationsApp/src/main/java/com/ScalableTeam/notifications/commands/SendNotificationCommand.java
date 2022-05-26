package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.utils.Command;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationCommand implements Command {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(Object body) throws Exception {
        NotificationSendRequest notification = (NotificationSendRequest) body;
        notificationsRepository.sendNotification(notification);
        return 200;
    }
}
