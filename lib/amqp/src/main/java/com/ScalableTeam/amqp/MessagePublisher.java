package com.ScalableTeam.amqp;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

public class MessagePublisher {

    private static final String HEADER_COMMAND = "Command";

    public static MessagePostProcessor getMessageHeaders(String responseQueue) {
        UUID correlationId = UUID.randomUUID();
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setReplyTo(responseQueue);
            messageProperties.setCorrelationId(correlationId.toString());
            return message;
        };
    }

    public static MessagePostProcessor processMessage(String commandName) {
        UUID correlationId = UUID.randomUUID();
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setCorrelationId(correlationId.toString());
            messageProperties.getHeaders().put(HEADER_COMMAND, commandName);
            return message;
        };
    }

    public static MessagePostProcessor processMessage(String commandName, String responseQueue) {
        UUID correlationId = UUID.randomUUID();
        return message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setCorrelationId(correlationId.toString());
            messageProperties.getHeaders().put(HEADER_COMMAND, commandName);
            messageProperties.setReplyTo(responseQueue);
            return message;
        };
    }
}
