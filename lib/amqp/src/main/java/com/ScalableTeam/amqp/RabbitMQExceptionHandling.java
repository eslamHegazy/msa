package com.ScalableTeam.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
@Slf4j
public class RabbitMQExceptionHandling {
    private final Config config;

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(config.getExceptions().getQueue()).build();
    }

    @Bean
    FanoutExchange deadLetterExchange() {
        return new FanoutExchange(config.getExceptions().getExchange());
    }

    @Bean
    Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange());
    }

    @RabbitListener(queues = "${mq.exceptions.queue}")
    public void processFailedMessages(Message message) {
        log.info("Received failed message: {}", message.toString());
    }
}
