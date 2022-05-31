package com.ScalableTeam.notifications.messaging;

import com.ScalableTeam.amqp.MessageQueues;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    public Queue queue() {
        return new Queue(MessageQueues.NOTIFICATIONS, true);
    }
}
