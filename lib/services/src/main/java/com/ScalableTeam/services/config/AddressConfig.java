package com.ScalableTeam.services.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "address")
public class AddressConfig {
    private Map<String, Integer> ports;
    private Map<String, String> ips;
    private int defaultPort;
}
