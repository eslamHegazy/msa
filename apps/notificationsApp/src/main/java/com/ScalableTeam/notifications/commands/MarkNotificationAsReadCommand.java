package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.Command;
import com.ScalableTeam.notifications.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.NotificationReadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarkNotificationAsReadCommand implements Command {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(Object body) throws Exception {
        NotificationReadRequest notificationRead = (NotificationReadRequest) body;
        notificationsRepository.markNotificationAsRead(notificationRead);
        return 200;
    }
}
