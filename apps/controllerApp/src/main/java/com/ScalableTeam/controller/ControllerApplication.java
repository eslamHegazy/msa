package com.ScalableTeam.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.controller",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.models"
//        ,
//        "com.ScalableTeam.services"
        , "com.ScalableTeam.utils"
})
@ConfigurationPropertiesScan
//@EnableEurekaClient
@PropertySource("classpath:message-queues.properties")
@PropertySource("classpath:address-config.properties")

public class ControllerApplication
//        extends ControllerService
{
    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
    }
}