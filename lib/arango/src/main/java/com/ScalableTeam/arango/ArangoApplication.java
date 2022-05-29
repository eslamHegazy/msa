package com.ScalableTeam.arango;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class ArangoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArangoApplication.class, args);
    }
}
