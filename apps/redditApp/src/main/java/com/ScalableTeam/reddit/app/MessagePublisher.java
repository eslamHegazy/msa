package com.ScalableTeam.reddit.app;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

public class MessagePublisher {
    protected MessagePostProcessor getMessageHeaders(String responseQueue) {
        UUID correlationId = UUID.randomUUID();
        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties
                    = message.getMessageProperties();
            messageProperties.setReplyTo(responseQueue);
            messageProperties.setCorrelationId(correlationId.toString());
            return message;
        };
        return messagePostProcessor;
    }
}
