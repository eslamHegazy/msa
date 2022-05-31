package com.ScalableTeam.services;

import com.ScalableTeam.services.config.AddressConfig;
import com.ScalableTeam.services.controllerserver.ControllerRequestsServer;
import com.ScalableTeam.utils.StringUtils;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
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
                controllerRequestsServer.setTcpPort(port);
                controllerRequestsServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
