package com.ScalableTeam.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.chat",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.models"
//        ,
//        "com.ScalableTeam.services"
})

@ConfigurationPropertiesScan
//@EnableEurekaClient
@PropertySource("classpath:message-queues.properties")
@PropertySource("classpath:address-config.properties")
public class ChatApplication
//        extends BaseService
{

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}

