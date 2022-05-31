package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.models.notifications.requests.NotificationReadRequest;
import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MarkNotificationAsReadCommand implements Command<NotificationReadRequest, ResponseEntity<HttpStatus>> {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public ResponseEntity<HttpStatus> execute(NotificationReadRequest body) {
        try {
            notificationsRepository.markNotificationAsRead(body);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
