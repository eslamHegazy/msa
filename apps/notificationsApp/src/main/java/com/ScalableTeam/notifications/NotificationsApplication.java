package com.ScalableTeam.notifications;

import com.ScalableTeam.notifications.utils.FirebaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.ScalableTeam.amqp",
                "com.ScalableTeam.notifications",
        }
)
@EnableEurekaClient
@PropertySource("classpath:message-queues.properties")
public class NotificationsApplication {

    public static void main(String[] args) {
        FirebaseInitializer.initialize();
        SpringApplication.run(NotificationsApplication.class, args);
    }
}
