package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.models.notifications.requests.NotificationReadRequest;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class NotificationsController {

    @Autowired
    private CommandsMapper commandsMapper;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/sendNotification")
    private void sendNotification(@RequestBody NotificationSendRequest notificationSendRequest) {
        String commandName = commandsMapper.getNotifications().get("sendNotification");
        log.info("Controller - Command: {}, Payload: {}", commandName, notificationSendRequest);
        rabbitMQProducer.publishAsynchronous(commandName, notificationSendRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getNotifications/{userId}")
    private Object getNotifications(@PathVariable String userId) {
        String commandName = commandsMapper.getNotifications().get("getNotifications");
        log.info("Controller - Command: {}, Payload: {}", commandName, userId);
        return rabbitMQProducer.publishSynchronous(commandName, userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/markNotificationAsRead")
    private void markNotificationAsRead(@RequestBody NotificationReadRequest notificationReadRequest) {
        String commandName = commandsMapper.getNotifications().get("markNotificationAsRead");
        log.info("Controller - Command: {}, Payload: {}", commandName, notificationReadRequest);
        rabbitMQProducer.publishAsynchronous(commandName, notificationReadRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteNotification")
    private void deleteNotification(@RequestBody NotificationDeleteRequest notificationDeleteRequest) {
        String commandName = commandsMapper.getNotifications().get("deleteNotification");
        log.info("Controller - Command: {}, Payload: {}", commandName, notificationDeleteRequest);
        rabbitMQProducer.publishAsynchronous(commandName, notificationDeleteRequest);
    }
}
