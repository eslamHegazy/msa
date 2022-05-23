package com.ScalableTeam.reddit.app.recommendations;

import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RecommendationsController {

    @Autowired
    private RedditsRecommendationsService redditsRecommendationsService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping(method = RequestMethod.GET,value="/redditRecommendations/{userNameId}")
    private String [] redditsRecommendations(@PathVariable String userNameId) throws Exception {
        log.info(generalConfig.getCommands().get("redditsRecommendations") + "Controller", userNameId);
        return redditsRecommendationsService.execute(userNameId);
    }

}
