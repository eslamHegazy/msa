package com.ScalableTeam.notifications.messaging;

import com.ScalableTeam.amqp.MessageQueues;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    public Queue requestQueue() {
        return new Queue(MessageQueues.REQUEST_NOTIFICATIONS, true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(MessageQueues.RESPONSE_NOTIFICATIONS, true);
    }
}
