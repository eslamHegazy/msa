package com.ScalableTeam.notifications.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "general")
@Data
public class LoggingConfig {
    private Map<String, String> commands;
    private String environment;
}
