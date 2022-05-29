package com.ScalableTeam.reddit;

import com.ScalableTeam.services.BaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.reddit",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.arango",
        "com.ScalableTeam.models",
        "com.ScalableTeam.services"
})
@ConfigurationPropertiesScan
//@EnableEurekaClient
@EnableCaching
@PropertySource("classpath:message-queues.properties")
public class RedditApplication extends BaseService {
    public static void main(String[] args) {
        SpringApplication.run(RedditApplication.class, args);
    }
}

