package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.models.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.utils.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteNotificationCommand implements Command {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(Object body) throws Exception {
        NotificationDeleteRequest notification = (NotificationDeleteRequest) body;
        notificationsRepository.deleteNotification(notification);
        return 200;
    }
}
