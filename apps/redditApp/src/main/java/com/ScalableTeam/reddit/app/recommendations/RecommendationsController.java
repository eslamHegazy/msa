package com.ScalableTeam.reddit.app.recommendations;

import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class RecommendationsController {

    @Autowired
    private RedditsRecommendationsService redditsRecommendationsService;
    @Autowired
    private GeneralConfig generalConfig;

    @RequestMapping("/redditRecommendations/{userNameId}")
    private String [] redditsRecommendations(@PathVariable String userNameId) throws Exception {
        log.info(generalConfig.getCommands().get("redditRecommendations") + "Controller", userNameId);
        return redditsRecommendationsService.execute(userNameId);
    }

}
