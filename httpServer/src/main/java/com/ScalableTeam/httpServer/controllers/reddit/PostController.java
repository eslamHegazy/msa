package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.reddit.VotePostForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @PostMapping
    public void createPost(@RequestBody Post post) throws Exception {
        String command = "createPost";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, post);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                post,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @GetMapping("{postId}")
    public Object getPost(@PathVariable String postId) throws Exception {
        String command = "getPost";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, postId);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                postId
        );
    }

    @GetMapping("{postId}/comments")
    public Object getPostComments(@PathVariable String postId) throws Exception {
        String command = "getPostComments";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, postId);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                postId
        );
    }

    @PostMapping("upvote/{id}")
    public void upvotePost(@RequestParam String userNameId, @PathVariable String id) {
        String command = "upvotePost";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        VotePostForm votePostForm = VotePostForm.builder()
                .postId(id)
                .userNameId(userNameId)
                .build();
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, votePostForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                votePostForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @PostMapping("downvote/{id}")
    public void downvotePost(@RequestParam String userNameId, @PathVariable String id) {
        String command = "downvotePost";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        VotePostForm votePostForm = VotePostForm.builder()
                .postId(id)
                .userNameId(userNameId)
                .build();
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, votePostForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                votePostForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @GetMapping("/popularPosts")
    public Object getPopularPosts() throws Exception {
        String command = "getPopularPosts";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, "");
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                ""
        );
    }
}