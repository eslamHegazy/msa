package com.ScalableTeam.arango;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@EnableArangoRepositories(basePackages = {"com.ScalableTeam.arango", "com.ScalableTeam.user.repository", "com.ScalableTeam.reddit.app.repository"})
public class DemoConfiguration implements ArangoConfiguration {

    @Value("${arangodb.host}")
    private String host;

    @Override
    public ArangoDB.Builder arango() {
        log.info("ArangoDB running on host: {}", host);
        return new ArangoDB.Builder().host(host, 8529).user("root").password(null);
    }

    @Override
    public String database() {
        return "spring-demo";
    }
}
