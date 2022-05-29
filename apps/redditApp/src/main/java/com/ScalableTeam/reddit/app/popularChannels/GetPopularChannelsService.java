package com.ScalableTeam.reddit.app.popularChannels;



import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class GetPopularChannelsService implements MyCommand {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private GeneralConfig generalConfig;

    private Post getValue(String cacheName, String key) {
        return (Post) cacheManager.getCache(cacheName).get(key);
    }
    @RabbitListener(queues = "${mq.queues.request.reddit.getPopularChannels}", returnExceptions = "true")
    public String listenToRequestQueue(Object body, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("getPopularChannels");
        log.info(indicator + "Service::Get popular Channels, CorrelationId={}", correlationId);
        return execute(null);
    }
    @Override
    public String execute(Object body) throws Exception {
        String cacheName = "popularChannelsCache";
        Set<String> keys = redisTemplate.keys(cacheName+"*");
        ArrayList<String> popularChannels = new ArrayList<>();
        System.err.println("hello1");
        if(keys!=null) {
            System.err.println("hello "+keys.size());
            for (String s : keys) {
                System.err.println(s + "hi hi");
                Cache.ValueWrapper v = cacheManager.getCache(cacheName).get(s.substring((cacheName+"::").length()));
                if (v != null)
                    popularChannels.add((String) v.get());
            }
        }
        return popularChannels.toString();
//        return (Map<String, Post>) redisTemplate.keys(cacheName+"*").parallelStream().map(key->{
//            Map<String,Post>cacheEntries=new HashMap<>();
//            cacheEntries.put(key,getValue(cacheName,key));
//            return cacheEntries ;
//        });
    }
//    @RabbitListener(queues = "${mq.queues.response.reddit.comment}")
//    public void receive(String response, Message message) {
//        String indicator = generalConfig.getCommands().get("comment");
//        String correlationId = message.getMessageProperties().getCorrelationId();
//        log.info(indicator + "Service:: COMMENT CorrelationId: {}, message: {}", correlationId, response);
//    }
}

