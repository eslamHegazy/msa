package com.ScalableTeam.reddit.app.caching;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.post.GetPostService;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/caching")
public class CachingController {
    @Autowired
    private CachingService cachingService;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private GetPostService getPostService;
    @Autowired
    private GeneralConfig generalConfig;
    @PostMapping("evict/postsCache")
    public String evictPostsCache() throws Exception {
        String indicator = generalConfig.getCommands().get("cachingService");
        log.info(indicator + "Controller");
        cachingService.evictAllEntriesOfPostsCache();
        return "Eviction postsCache done";
    }
    @PostMapping("evict/popularPostsCache")
    public String evictPopularPostsCache() throws Exception {
        String indicator = generalConfig.getCommands().get("cachingService");
        log.info(indicator + "Controller");
        cachingService.evictAllEntriesOfPopularPostsCache();
        return "Eviction popularPostsCache done";
    }
    @PostMapping("evict/popularChannelsCache")
    public String evictPopularChannelsCache() throws Exception {
        String indicator = generalConfig.getCommands().get("cachingService");
        log.info(indicator + "Controller");
        cachingService.evictAllEntriesOfPopularChannelsCache();
        return "Eviction popularChannelsCache done";
    }

}
