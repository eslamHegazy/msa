package com.ScalableTeam.controller;

import com.ScalableTeam.controller.client.ControllerClient;
import com.ScalableTeam.controller.client.ControllerClientExecutor;
import com.ScalableTeam.controller.config.AddressConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class ControllerPublisher {
    private final ControllerClientExecutor controllerClientExecutor;
    private final ControllerClient controllerClient;
    private final AddressConfig addressConfig;

    public void handleRequest(String request) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(request);
            if (!jsonObject.has("app") || !jsonObject.has("command"))
                throw new JSONException("Either app or command in controller request not found");
            sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("Controller request does not have required fields or unserializable string.");
            throw e;
        }
    }

    private void sendMessage(JSONObject request) throws Exception {
        sendMessageToApp(request.toString(), request.getString("app"));
    }

    private void sendMessageToApp(String message, String instanceName) throws Exception {
        sendMessage(addressConfig.getIps().get(instanceName), addressConfig.getPorts().get(instanceName), message);
    }

    private void sendMessage(String ip, int port, String message) throws Exception {
        log.info("Message sent to app with ip {} and port {}", ip, port);
        controllerClientExecutor.setCommand(message);
        controllerClient.setTcp(ip, port);
        controllerClient.start();
    }

    private void sendMessage(List<String> ips, int port, String message) throws Exception {
        for (String ip : ips)
            sendMessage(ip, port, message);
    }
}
