package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.MessageQueues;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.models.notifications.requests.NotificationReadRequest;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class NotificationsController {

    @Autowired
    private CommandsMapper commandsMapper;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/sendNotification")
    private ResponseEntity<HttpStatus> sendNotification(@RequestBody NotificationSendRequest notificationSendRequest) {
        String commandName = commandsMapper.getNotifications().get("sendNotification");
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", MessageQueues.REQUEST_NOTIFICATIONS, commandName, notificationSendRequest);
        rabbitMQProducer.publishAsynchronousToQueue(MessageQueues.REQUEST_NOTIFICATIONS, commandName, notificationSendRequest, MessageQueues.RESPONSE_NOTIFICATIONS);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getNotifications/{userId}")
    private Object getNotifications(@PathVariable String userId) {
        String commandName = commandsMapper.getNotifications().get("getNotifications");
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", MessageQueues.REQUEST_NOTIFICATIONS, commandName, userId);
        return rabbitMQProducer.publishSynchronous(MessageQueues.REQUEST_NOTIFICATIONS, commandName, userId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/markNotificationAsRead")
    private Object markNotificationAsRead(@RequestBody NotificationReadRequest notificationReadRequest) {
        String commandName = commandsMapper.getNotifications().get("markNotificationAsRead");
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", MessageQueues.REQUEST_NOTIFICATIONS, commandName, notificationReadRequest);
        return rabbitMQProducer.publishSynchronous(MessageQueues.REQUEST_NOTIFICATIONS, commandName, notificationReadRequest);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteNotification")
    private Object deleteNotification(@RequestBody NotificationDeleteRequest notificationDeleteRequest) {
        String commandName = commandsMapper.getNotifications().get("deleteNotification");
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", MessageQueues.REQUEST_NOTIFICATIONS, commandName, notificationDeleteRequest);
        return rabbitMQProducer.publishSynchronous(MessageQueues.REQUEST_NOTIFICATIONS, commandName, notificationDeleteRequest);
    }
}
