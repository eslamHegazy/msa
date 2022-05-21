package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.Command;
import com.ScalableTeam.notifications.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationCommand implements Command {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(Object body) throws Exception {
        NotificationRequest notification = (NotificationRequest) body;
        notificationsRepository.sendNotification(notification);
        return 200;
    }
}
