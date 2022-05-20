package com.ScalableTeam.notifications;

import com.ScalableTeam.amqp.RabbitMQMessageProducer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.ScalableTeam.notifications", "com.ScalableTeam.amqp",})
@EnableEurekaClient
public class NotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
        FirebaseInitializer.initialize();
    }

    @Bean
    CommandLineRunner commandLineRunner(RabbitMQMessageProducer producer) {
        return args -> {
            producer.publish(new Person("Maria", 23), "amq.direct", "notif");
        };
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {
    private String name;
    private int age;
}