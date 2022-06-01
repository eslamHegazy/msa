package com.ScalableTeam.notifications.commands;

import com.ScalableTeam.models.notifications.requests.DeviceTokenRequest;
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
public class RegisterDeviceTokenCommand implements Command<DeviceTokenRequest, ResponseEntity<HttpStatus>> {

    private final NotificationsRepository notificationsRepository;

    @Override
    public ResponseEntity<HttpStatus> execute(DeviceTokenRequest body) {
        try {
            notificationsRepository.registerDeviceToken(body);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
