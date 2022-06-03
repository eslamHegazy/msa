package com.ScalableTeam.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.media",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.models"
//        , "com.ScalableTeam.services"
})
//@EnableEurekaClient
@PropertySource("classpath:message-queues.properties")
@PropertySource("classpath:address-config.properties")

public class MediaAppApplication
//        extends BaseService
{

    public static void main(String[] args) {
        SpringApplication.run(MediaAppApplication.class, args);
    }

}
