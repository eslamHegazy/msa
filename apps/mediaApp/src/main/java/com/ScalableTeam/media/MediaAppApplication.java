package com.ScalableTeam.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.media",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.models",
        "com.ScalableTeam.services"
})
//@EnableEurekaClient
@PropertySource("classpath:message-queues.properties")
public class MediaAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaAppApplication.class, args);
    }

}
