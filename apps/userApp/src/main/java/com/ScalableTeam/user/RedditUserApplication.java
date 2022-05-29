package com.ScalableTeam.user;

import com.ScalableTeam.services.BaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {
        "com.ScalableTeam.user",
        "com.ScalableTeam.amqp",
        "com.ScalableTeam.arango",
        "com.ScalableTeam.models",
        "com.ScalableTeam.services"
})
@ConfigurationPropertiesScan
@EnableCaching
@PropertySource("classpath:message-queues.properties")
public class RedditUserApplication extends BaseService {

    public static void main(String[] args) {
        SpringApplication.run(RedditUserApplication.class, args);
    }

}
