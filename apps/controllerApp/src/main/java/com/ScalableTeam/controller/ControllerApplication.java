package com.ScalableTeam.controller;

import com.ScalableTeam.controller.server.ControllerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.controller",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.models",
        "com.ScalableTeam.services"
})
@ConfigurationPropertiesScan
//@EnableEurekaClient
@PropertySource("classpath:message-queues.properties")
public class ControllerApplication extends ControllerService {
    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }
}