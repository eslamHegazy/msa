package com.ScalableTeam.reddit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "postgresql")
@Data
public class PostgresqlConfig {
    private int databasePort;
    private String databaseName;
}
