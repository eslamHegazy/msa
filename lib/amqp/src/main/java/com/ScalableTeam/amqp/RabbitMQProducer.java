package com.ScalableTeam.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import static com.ScalableTeam.amqp.MessagePublisher.processMessage;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;

    public void publishAsynchronous(String commandName, Object payload) {
        log.info("Publishing. Command: {}, Payload: {}", commandName, payload);
        amqpTemplate.convertAndSend(commandName, payload, processMessage(commandName));
        log.info("Published. Command: {}, Payload: {}", commandName, payload);
    }

    public void publishAsynchronousToQueue(String commandName, String responseQueue, Object payload, String exchange, String routingKey) {
        log.info("Publishing to {} using routingKey {}. Command: {}, Payload: {}", exchange, routingKey, commandName, payload);
        amqpTemplate.convertAndSend(exchange, routingKey, payload, processMessage(commandName, responseQueue));
        log.info("Published to {} using routingKey {}. Command: {}, Payload: {}", exchange, routingKey, commandName, payload);
    }

    public Object publishSynchronous(String commandName, Object payload, String exchange, String routingKey) {
        log.info("Publishing to {} using routingKey {}. Command: {}, Payload: {}", exchange, routingKey, commandName, payload);
        return amqpTemplate.convertSendAndReceiveAsType(exchange, routingKey, payload, processMessage(commandName),
                new ParameterizedTypeReference<>() {
                });
    }
}
