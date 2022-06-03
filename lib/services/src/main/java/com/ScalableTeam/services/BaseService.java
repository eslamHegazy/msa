package com.ScalableTeam.services;

import com.ScalableTeam.services.config.AddressConfig;
import com.ScalableTeam.services.controllerserver.ControllerRequestsServer;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.Map;

@NoArgsConstructor
public class BaseService {

    @Autowired
    private ControllerRequestsServer controllerRequestsServer;
    @Value("${spring.application.name: application}")
    private String appName;
    @Autowired
    private AddressConfig addressConfig;

    @PostConstruct
    protected void listenToController() {
        new Thread(() -> {
            try {
                int port = addressConfig.getDefaultPort();
                Map<String, Integer> m = addressConfig.getPorts();
                if (m.containsKey(appName))
                    port = m.get(appName);
                controllerRequestsServer.setTcp("127.0.0.1", port);
                controllerRequestsServer.start();
            } catch (InterruptedException | UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
