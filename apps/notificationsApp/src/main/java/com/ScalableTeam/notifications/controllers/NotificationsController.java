package com.ScalableTeam.notifications.controllers;

import com.ScalableTeam.notifications.commands.DeleteNotificationCommand;
import com.ScalableTeam.notifications.commands.GetNotificationsCommand;
import com.ScalableTeam.notifications.commands.MarkNotificationAsReadCommand;
import com.ScalableTeam.notifications.commands.SendNotificationCommand;
import com.ScalableTeam.notifications.config.GeneralConfig;
import com.ScalableTeam.notifications.models.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.models.requests.NotificationReadRequest;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import com.ScalableTeam.notifications.models.responses.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Controller
public class NotificationsController {

    @Autowired
    private SendNotificationCommand sendNotificationCommand;

    @Autowired
    private GetNotificationsCommand getNotificationsCommand;

    @Autowired
    private MarkNotificationAsReadCommand markNotificationAsReadCommand;

    @Autowired
    private DeleteNotificationCommand deleteNotificationCommand;

    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.POST, value = "/sendNotification")
    private Integer sendNotification(@RequestBody NotificationSendRequest notificationSendRequest) throws Exception {
        log.info(generalConfig.getCommands().get("sendNotification"), notificationSendRequest);
        return sendNotificationCommand.execute(notificationSendRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getNotifications/{userId}")
    private List<NotificationResponse> getNotifications(@PathVariable String userId) throws Exception {
        log.info(generalConfig.getCommands().get("getNotifications"), userId);
        return getNotificationsCommand.execute(userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/markNotificationAsRead")
    private Integer markNotificationAsRead(@RequestBody NotificationReadRequest notificationReadRequest) throws Exception {
        log.info(generalConfig.getCommands().get("markNotificationAsRead"), notificationReadRequest);
        return markNotificationAsReadCommand.execute(notificationReadRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteNotification")
    private Integer deleteNotification(@RequestBody NotificationDeleteRequest notificationDeleteRequest) throws Exception {
        log.info(generalConfig.getCommands().get("deleteNotification"), notificationDeleteRequest);
        return deleteNotificationCommand.execute(notificationDeleteRequest);
    }
}
