package com.ScalableTeam.user.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    public static final String QUEUE = "user-app";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

//    @Bean
//    public MessageConverter messageConverter() {
//        return  new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public AmqpTemplate template(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(messageConverter());
//        return  template;
//    }
}
