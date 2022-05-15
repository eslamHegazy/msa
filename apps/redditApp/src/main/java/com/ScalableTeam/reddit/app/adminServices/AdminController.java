package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdminController {
    @Autowired
    private CreateChannelService createChannelService;
    @Autowired
    private AssignModeratorsService assignModeratorsService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.POST,value = "/channels")
    private String createChannel(@RequestBody CreateChannelForm createChannelForm) throws Exception {
        log.info(generalConfig.getCommands().get("createChannel") + "Controller", createChannelForm);
        return createChannelService.execute(createChannelForm);
    }
    @RequestMapping(method = RequestMethod.PUT,value="/channels")
    private String assignModerators(@RequestBody AssignModeratorsForm assignModeratorsForm) throws Exception {
        log.info(generalConfig.getCommands().get("assignModerators") + "Controller", assignModeratorsForm);
        return assignModeratorsService.execute(assignModeratorsForm);
    }


}
