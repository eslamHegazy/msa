package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class GetPostService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    //    @Resource(name="redisTemplate")
//    private HashOperations<String, String, Post> hashOperations;
    @RabbitListener(queues = "${mq.queues.request.reddit.getPost}", returnExceptions = "true")
    public String listenToRequestQueue(String postId, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Get Post Form={}", commandName, correlationId, postId);
        return execute(postId);
    }

    @Override
    public String execute(Object postId) throws Exception {
        // TODO: CHECK THE USER IS AUTHENTICATED AND IS SAME USER IN POST
        try {
            String postIdString = (String) postId;
            log.info("Service::Get Post Form Form={}", postIdString);
            //Make necessary checks that the user follows this channel
            return continueExecuting(postIdString);
        } catch (Exception e) {
            throw new Exception("Error: Couldn't add post");
//            return "Error: Couldn't add post";
        }
    }

    @Cacheable(cacheNames = "postsCache", key = "#postId")
    public String continueExecuting(String postId) {
        return postRepository.findById(postId).get().toString();
    }
}