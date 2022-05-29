package com.ScalableTeam.httpServer.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.httpServer",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.models",
        "com.ScalableTeam.httpServer.controllers"
})
@ConfigurationPropertiesScan
@PropertySource("classpath:message-queues.properties")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
