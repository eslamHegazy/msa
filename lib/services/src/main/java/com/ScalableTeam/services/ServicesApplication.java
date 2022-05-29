package com.ScalableTeam.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.services",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.utils"
})
public class ServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServicesApplication.class, args);
    }
}
