package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.models.notifications.requests.DeviceTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;

@Slf4j
@RestController
@Controller
public class DeviceTokensController {

    @Autowired
    private Config config;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.PUT, value = "/registerDeviceToken")
    private void registerDeviceToken(@RequestBody DeviceTokenRequest deviceTokenRequest) {
        String commandName = "registerDeviceToken";

        log.info(commandName + "::Controller, Body: {}", deviceTokenRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(deviceTokenRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/unregisterDeviceToken")
    private void unregisterDeviceToken(@RequestBody DeviceTokenRequest deviceTokenRequest) {
        String commandName = "unregisterDeviceToken";

        log.info(commandName + "::Controller, Body: {}", deviceTokenRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(deviceTokenRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }
}
