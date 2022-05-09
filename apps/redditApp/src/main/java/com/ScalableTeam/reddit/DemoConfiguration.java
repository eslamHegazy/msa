package com.ScalableTeam.reddit;


import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = {"com.ScalableTeam.reddit"})
public class DemoConfiguration implements ArangoConfiguration {

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder().host("localhost", 8529).user("root").password(null);
    }

    @Override
    public String database() {
        return "spring-demo";
    }
}
