package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.arango.User;
import com.ScalableTeam.arango.UserRepository;
import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.repository.PostRepository;
import com.arangodb.springframework.core.ArangoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class CreatePostService implements MyCommand {
    @Autowired
    private ArangoOperations operations;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    //    @Resource(name="redisTemplate")
//    private HashOperations<String, String, Post> hashOperations;
    @RabbitListener(queues = "${mq.queues.request.reddit.createPost}")
    public Post listenToRequestQueue(Post post, Message message, @Header(MessagePublisher.HEADER_COMMAND) String commandName) throws Exception {
//        System.err.println("listening");
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Queue Listener::Command={}, CorrelationId={}, Create Post Form={}", commandName, correlationId, post);
        return execute(post);
    }

    @Override
    public Post execute(Object postObj) throws Exception {
        // TODO: CHECK THE USER IS AUTHENTICATED AND IS SAME USER IN POST
        try {
            Post post = (Post) postObj;
            log.info("Service::Create Post Form={}", post);
            //Make necessary checks that the user follows this channel
            final Optional<User> postCreatorOptional = userRepository.findById(post.getUserNameId());
            if (postCreatorOptional.isEmpty() ||
                    !postCreatorOptional.get().getFollowedChannels().containsKey(post.getChannelId())) {
                throw new Exception();
            }
            Instant time = Instant.now();
            post.setTime(Date.from(time));
            postRepository.save(post);
            continueExecuting(post, post.getId());
            //System.err.println("returning after listening and executing");
            return post;
        } catch (Exception e) {
            throw new Exception("Error: Couldn't add post");
//            return "Error: Couldn't add post";
        }
    }

    @CachePut(cacheNames = "postsCache", key = "#postId")
    public String continueExecuting(Post post, String postId) {
        return post.toString();
    }

    @RabbitListener(queues = "${mq.queues.response.reddit.createPost}")
    public void receive(Post response, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Response Queue Listener::CorrelationId={}, response={}", correlationId, response);
    }
}
