package com.ScalableTeam.services;

import com.ScalableTeam.services.controllerserver.ControllerRequestsServer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@NoArgsConstructor
public class BaseService {

    @Autowired
    private ControllerRequestsServer controllerRequestsServer;

    @PostConstruct
    protected void listenToController() {
        new Thread(() -> {
            try {
                controllerRequestsServer.setTcpPort(3001);
                controllerRequestsServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
