package com.ScalableTeam.services.managers;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Slf4j
@AllArgsConstructor
public class ControlManager {
    private final ConsumerManager consumerManager;

    public void handleControllerMessage(JSONObject message) throws JSONException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String command = message.getString("command");
        String app = message.getString("app");
        log.info("{} has received a message from the controller!\n{}\n", app, message);
        Method method = ControlManager.class.getDeclaredMethod(command, Integer.class);
        method.invoke(this, message.getInt("args"));
//        try {
//            boolean hasArgs = message.has("args");
//            method.invoke(this, hasArgs ? message.optJSONArray(Controller.ARGS).toList().toArray() : null);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    public void setMaxThreadsCount(Integer maxThreadsCount) {
        consumerManager.changeMaxThreadCount(maxThreadsCount);
    }
    public void setMinThreadsCount(Integer minThreadsCount) {
        consumerManager.changeMinThreadCount(minThreadsCount);
    }

}
