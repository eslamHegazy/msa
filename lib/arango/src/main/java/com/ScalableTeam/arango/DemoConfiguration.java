package com.ScalableTeam.arango;

import com.arangodb.ArangoDB;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = {"com.ScalableTeam.arango", "com.ScalableTeam.user.repository", "com.ScalableTeam.reddit.app.repository"})
public class DemoConfiguration implements ArangoConfiguration {

    @Value("$")

    @Override
    public ArangoDB.Builder arango() {
        return new ArangoDB.Builder().user("root").password(null);
    }

    @Override
    public String database() {
        return "spring-demo";
    }
}
