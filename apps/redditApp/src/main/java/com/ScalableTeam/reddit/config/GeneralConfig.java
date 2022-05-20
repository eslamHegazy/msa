package com.ScalableTeam.reddit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "general")
@Data
public class GeneralConfig {
    private Map<String, String> commands;
}
