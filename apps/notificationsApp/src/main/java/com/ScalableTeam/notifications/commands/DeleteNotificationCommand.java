package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteNotificationCommand implements Command<NotificationDeleteRequest, ResponseEntity<HttpStatus>> {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public ResponseEntity<HttpStatus> execute(NotificationDeleteRequest body) {
        try {
            notificationsRepository.deleteNotification(body);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
