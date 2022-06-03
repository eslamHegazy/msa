package com.ScalableTeam.controller;

import com.ScalableTeam.controller.client.ControllerClient;
import com.ScalableTeam.controller.client.ControllerClientExecutor;
import com.ScalableTeam.services.config.AddressConfig;
import com.ScalableTeam.services.managers.ByteClassLoader;
import com.ScalableTeam.services.managers.ClassManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
            handleMainMessageAttributesChecks(jsonObject);
            handleAddCommand(jsonObject);
            sendMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            log.error("Controller request does not have required fields or unserializable string.");
            throw e;
        }
    }

    private void handleMainMessageAttributesChecks(JSONObject jsonObject) throws JSONException {
        if (!jsonObject.has("app") || !jsonObject.has("command"))
            throw new JSONException("Either app or command in controller request not found");
    }

    private void handleAddCommand(JSONObject jsonObject) throws JSONException, IOException {
        if (jsonObject.has("hasCommand") &&
                jsonObject.optBoolean("hasCommand") &&
                jsonObject.has("args") &&
                jsonObject.optJSONArray("args") != null &&
                jsonObject.optJSONArray("args").length() > 0 &&
                jsonObject.optJSONArray("args").get(0) instanceof String
        ) {
            System.out.println(jsonObject.optJSONArray("args").get(0));
            String byteString = org.apache.commons.codec.binary.Base64.encodeBase64String(
                    ByteClassLoader.readClassFileAsBytes((String)
                            jsonObject.optJSONArray("args").get(0))
            );
            System.out.println(byteString);
            jsonObject
                    .optJSONArray("args")
                    .put(1, byteString);
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

        new Thread(() -> {
            try {
                controllerClient.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage(List<String> ips, int port, String message) throws Exception {
        for (String ip : ips)
            sendMessage(ip, port, message);
    }
}