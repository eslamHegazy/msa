package com.ScalableTeam.amqp;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

public class MessagePublisher {

    public static MessagePostProcessor getMessageHeaders(String responseQueue) {
        UUID correlationId = UUID.randomUUID();
        return message -> {
            MessageProperties messageProperties
                    = message.getMessageProperties();
            messageProperties.setReplyTo(responseQueue);
            messageProperties.setCorrelationId(correlationId.toString());
            return message;
        };
    }
}
