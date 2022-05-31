package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.utils.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetNotificationsCommand implements Command<String, ResponseEntity<?>> {

    @Autowired
    private NotificationsRepository notificationsRepository;

    @Override
    public ResponseEntity<?> execute(String body) {
        try {
            return new ResponseEntity<>(notificationsRepository.getNotifications(body), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
