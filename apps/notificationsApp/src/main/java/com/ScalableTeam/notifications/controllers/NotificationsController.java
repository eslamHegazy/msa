package com.ScalableTeam.notifications.controllers;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.notifications.config.GeneralConfig;
import com.ScalableTeam.notifications.models.requests.NotificationDeleteRequest;
import com.ScalableTeam.notifications.models.requests.NotificationReadRequest;
import com.ScalableTeam.notifications.models.requests.NotificationSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;

@Slf4j
@RestController
@Controller
public class NotificationsController {

    @Autowired
    private Config config;

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/sendNotification")
    private void sendNotification(@RequestBody NotificationSendRequest notificationSendRequest) {
        String commandName = "sendNotification";
        String indicator = generalConfig.getCommands().get(commandName);

        log.info(indicator + "Controller, Body: {}", notificationSendRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(notificationSendRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getNotifications/{userId}")
    private Object getNotifications(@PathVariable String userId) {
        String commandName = "getNotifications";
        String indicator = generalConfig.getCommands().get(commandName);

        log.info(indicator + "Controller, Body: {}", userId);

        return rabbitMQProducer.publishSynchronous(userId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/markNotificationAsRead")
    private void markNotificationAsRead(@RequestBody NotificationReadRequest notificationReadRequest) {
        String commandName = "markNotificationAsRead";
        String indicator = generalConfig.getCommands().get(commandName);

        log.info(indicator + "Controller, Body: {}", notificationReadRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(notificationReadRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteNotification")
    private void deleteNotification(@RequestBody NotificationDeleteRequest notificationDeleteRequest) {
        String commandName = "deleteNotification";
        String indicator = generalConfig.getCommands().get(commandName);

        log.info(indicator + "Controller, Body: {}", notificationDeleteRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(notificationDeleteRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }
}
