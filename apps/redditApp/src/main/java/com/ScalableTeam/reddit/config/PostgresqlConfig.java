package com.ScalableTeam.reddit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "postgres")
@Data
public class PostgresqlConfig {
    private String host;
    private String port;
    private String username;
    private String password;
    private String dbName;
}
