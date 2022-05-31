package com.ScalableTeam.httpServer.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "commands")
public class CommandsMapper {

    private Map<String, String> user;
    private Map<String, String> reddit;
    private Map<String, String> chat;
    private Map<String, String> media;
    private Map<String, String> notifications;
}
