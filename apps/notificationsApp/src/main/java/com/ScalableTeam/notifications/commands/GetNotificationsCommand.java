package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.notifications.data.NotificationsRepository;
import com.ScalableTeam.notifications.utils.Command;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class GetNotificationsCommand implements Command<String, ResponseEntity<?>> {

    private final NotificationsRepository notificationsRepository;

    @Override
    public ResponseEntity<?> execute(String body) {
        try {
            return new ResponseEntity<>(notificationsRepository.getNotifications(body), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
