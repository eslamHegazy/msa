package com.ScalableTeam.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import static com.ScalableTeam.amqp.MessagePublisher.processMessage;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;

    public void publishAsynchronous(String queueName, String commandName, Object payload) {
        log.info("Publishing. Queue: {}, Command: {}, Payload: {}", queueName, commandName, payload);
        amqpTemplate.convertAndSend(queueName, payload, processMessage(commandName));
        log.info("Published. Queue: {}, Command: {}, Payload: {}", queueName, commandName, payload);
    }

    public void publishAsynchronousToQueue(String queueName, String commandName, Object payload, String responseQueue) {
        log.info("Publishing. Queue: {}, Command: {}, Payload: {}, ResponseQueue: {}", queueName, commandName, payload, responseQueue);
        amqpTemplate.convertAndSend(queueName, payload, processMessage(commandName, responseQueue));
        log.info("Published. Queue: {}, Command: {}, Payload: {}, ResponseQueue: {}", queueName, commandName, payload, responseQueue);
    }

    public Object publishSynchronous(String queueName, String commandName, Object payload) {
        log.info("Publishing. Queue: {}, Command: {}, Payload: {}", queueName, commandName, payload);
        Object result = amqpTemplate.convertSendAndReceive(queueName, payload, processMessage(commandName));
        log.info("Published. Queue: {}, Command: {}, Payload: {}", queueName, commandName, payload);
        return result;
    }
}
