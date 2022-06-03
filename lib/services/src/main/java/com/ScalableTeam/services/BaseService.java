package com.ScalableTeam.services;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseService {

//    @Autowired
//    private ControllerRequestsServer controllerRequestsServer;
//    @Value("${spring.application.name: application}")
//    private String appName;
//    @Autowired
//    private AddressConfig addressConfig;

//    @PostConstruct
//    protected void listenToController() {
//        new Thread(() -> {
//            try {
//                int port = addressConfig.getDefaultPort();
//                Map<String, Integer> m = addressConfig.getPorts();
//                if (m.containsKey(appName))
//                    port = m.get(appName);
//                controllerRequestsServer.setTcpPort(port);
//                controllerRequestsServer.start();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
}
