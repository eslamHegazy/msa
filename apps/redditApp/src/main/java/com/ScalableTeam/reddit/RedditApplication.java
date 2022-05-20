package com.ScalableTeam.reddit;

import com.ScalableTeam.reddit.app.runner.CrudRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableEurekaClient
@EnableCaching
@ComponentScan("com.ScalableTeam.reddit")
public class RedditApplication {

    public static void main(String[] args) {
//		Class<?>[] runner = new Class<?>[]{CrudRunner.class};
//		System.exit(SpringApplication.exit(SpringApplication.run(runner, args)));
        SpringApplication.run(RedditApplication.class, args);
    }

}
