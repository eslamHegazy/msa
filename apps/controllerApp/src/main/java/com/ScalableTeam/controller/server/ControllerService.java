package com.ScalableTeam.controller.server;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@NoArgsConstructor
public class ControllerService {

    @Autowired
    private ControllerServer controllerServer;

    @PostConstruct
    protected void listenToController() {
        new Thread(() -> {
            try {
                controllerServer.setTcpPort(3002);
                controllerServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
