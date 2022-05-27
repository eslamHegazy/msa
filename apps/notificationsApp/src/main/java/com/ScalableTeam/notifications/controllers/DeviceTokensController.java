package com.ScalableTeam.notifications.controllers;

import com.ScalableTeam.notifications.commands.RegisterDeviceTokenCommand;
import com.ScalableTeam.notifications.commands.UnregisterDeviceTokenCommand;
import com.ScalableTeam.notifications.config.GeneralConfig;
import com.ScalableTeam.notifications.models.requests.DeviceTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Controller
public class DeviceTokensController {

    @Autowired
    private RegisterDeviceTokenCommand registerDeviceTokenCommand;

    @Autowired
    private UnregisterDeviceTokenCommand unregisterDeviceTokenCommand;

    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.PUT, value = "/registerDeviceToken")
    private Integer registerDeviceToken(@RequestBody DeviceTokenRequest deviceTokenRequest) throws Exception {
        log.info(generalConfig.getCommands().get("registerDeviceToken"), deviceTokenRequest);
        return registerDeviceTokenCommand.execute(deviceTokenRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/unregisterDeviceToken")
    private Integer unregisterDeviceToken(@RequestBody DeviceTokenRequest deviceTokenRequest) throws Exception {
        log.info(generalConfig.getCommands().get("unregisterDeviceToken"), deviceTokenRequest);
        return unregisterDeviceTokenCommand.execute(deviceTokenRequest);
    }
}
