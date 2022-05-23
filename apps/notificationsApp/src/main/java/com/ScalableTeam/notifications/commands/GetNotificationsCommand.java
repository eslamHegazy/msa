package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.utils.Command;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetNotificationsCommand implements Command {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public List<NotificationResponse> execute(Object body) throws Exception {
        String userId = String.valueOf(body);
        return notificationsRepository.getNotifications(userId);
    }
}
