package com.ScalableTeam.notifications.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String QUEUE_NAME = "notifications_service";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }
}
