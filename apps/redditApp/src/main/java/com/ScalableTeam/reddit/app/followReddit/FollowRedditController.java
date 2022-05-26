package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.app.requestForms.FollowRedditForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class FollowRedditController {
    @Autowired
    private FollowRedditService followRedditService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.POST,value = "/followReddit")
    private String followReddit(@RequestBody FollowRedditForm followRedditForm){
        log.info(generalConfig.getCommands().get("followReddit") + "Controller", followRedditForm);
        return followRedditService.execute(followRedditForm);
    }
}