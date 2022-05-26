package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Comment;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.config.GeneralConfig;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class GetPopularPostsService implements MyCommand {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private GeneralConfig generalConfig;

    private Post getValue(String cacheName, String key) {
        return (Post) cacheManager.getCache(cacheName).get(key);
    }
    @RabbitListener(queues = "${mq.queues.request.reddit.getPopularPosts}")
    public String listenToRequestQueue(Object body, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get("getPopularPosts");
        log.info(indicator + "Service::Get popular Posts, CorrelationId={}", correlationId);
        return execute(null);
    }
    @Override
    public String execute(Object body) throws Exception {
        String cacheName = "popularPostsCache";
        Set<String> keys = redisTemplate.keys("*");
        ArrayList<String> popularPosts = new ArrayList<>();
        if(keys!=null) {
            for (String s : keys) {
                System.err.println(s + "hi hi");
                Cache.ValueWrapper v = cacheManager.getCache(cacheName).get(s.substring((cacheName+"::").length()));
                if (v != null)
                    popularPosts.add((String) v.get());
            }
        }
        return popularPosts.toString();
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
