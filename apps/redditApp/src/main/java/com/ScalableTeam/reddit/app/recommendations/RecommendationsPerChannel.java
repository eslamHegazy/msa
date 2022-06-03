package com.ScalableTeam.reddit.app.recommendations;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.caching.CachingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class RecommendationsPerChannel implements MyCommand {

    @Autowired
    private CachingService cachingService;

    @Override
    public String execute(Object body) throws Exception {
        String redditId = (String) body;
        log.info("Service::Reddit specific recommendations for ={}", redditId);
        return "{caching=80}";
//                cachingService.getRecommendations(redditId);
    }
}
