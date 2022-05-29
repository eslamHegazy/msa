package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteNotificationCommand implements Command<NotificationDeleteRequest, Integer> {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public Integer execute(NotificationDeleteRequest body) throws Exception {
        notificationsRepository.deleteNotification(body);
        return 200;
    }
}
