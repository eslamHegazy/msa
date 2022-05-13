package com.ScalableTeam.reddit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan

public class RedditApplication {

    public static void main(String[] args) {
//		Class<?>[] runner = new Class<?>[]{CrudRunner.class};
//		System.exit(SpringApplication.exit(SpringApplication.run(runner, args)));
        SpringApplication.run(RedditApplication.class, args);
    }

}

