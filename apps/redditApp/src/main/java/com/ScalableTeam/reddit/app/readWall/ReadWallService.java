package com.ScalableTeam.reddit.app.readWall;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.caching.CachingService;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class ReadWallService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CachingService cachingService;

    @RabbitListener(queues = "${mq.queues.request.reddit.readWall}", returnExceptions = "true")
    public String listenToRequestQueue(String userNameIdString, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Read Wall Form={}", commandName, correlationId, userNameIdString);
        return execute(userNameIdString);
    }

    @Override

    public String execute(Object userNameIdString) throws Exception {
        String userNameId = (String) userNameIdString;
        log.info("Service::Read Wall Form={}", userNameId);
        return cachingService.getWall(userNameId);
    }


//    @Cacheable(cacheNames = "postsCache")
//    private Post[]getPostsFromFollowedChannels(String newLatestReadPostId, HashMap<String,Boolean>followedChannels){
//        return postRepository.getPostsByTimeAndChannel(newLatestReadPostId, followedChannels);
//    }
//    @Cacheable(cacheNames = "postsCache")
//    private Post[]getPostsFromFollowedUsers(String newLatestReadPostId,HashMap<String,Boolean>followedUsers){
//        return postRepository.getPostsByTimeAndUser(newLatestReadPostId,followedUsers);
//    }

}
