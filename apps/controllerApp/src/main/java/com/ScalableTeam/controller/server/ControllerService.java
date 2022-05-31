package com.ScalableTeam.controller.server;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@NoArgsConstructor
public class ControllerService {

    @Autowired
    private ControllerServer controllerServer;
    @Value("${port}")
    private int port;

    @PostConstruct
    protected void listenToController() {
        new Thread(() -> {
            try {
                controllerServer.setTcpPort(port);
                controllerServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
