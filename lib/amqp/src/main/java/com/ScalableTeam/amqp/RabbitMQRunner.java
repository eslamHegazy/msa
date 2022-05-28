package com.ScalableTeam.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQReceiver receiver;
    private final Config config;

    public RabbitMQRunner(RabbitMQReceiver receiver, RabbitTemplate rabbitTemplate, Config config) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("Sending message...");
//        rabbitTemplate.convertAndSend(config.getExchange(), "CREATE_POST_REQUEST", "Hello from RabbitMQ!");
//        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }
}
