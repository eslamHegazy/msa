package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.models.notifications.responses.NotificationResponse;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetNotificationsCommand implements Command<String, List<String>> {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public List<String> execute(String body) throws Exception {
//        return notificationsRepository.getNotifications(body);
        return List.of("maria");
    }
}
