package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.LegacyRabbitMQProducer;
import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.arango.Post;
import com.ScalableTeam.models.reddit.VotePostForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private Config config;

    @Autowired
    private LegacyRabbitMQProducer rabbitMQProducer;

    @PostMapping
    public void createPost(@RequestBody Post post) throws Exception {
        String commandName = "createPost";
        log.info(commandName + " Controller", post);
        MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                post,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get("createPost"),
                messagePostProcessor);
    }

    @GetMapping("{postId}")
    private Object getPost(@PathVariable String postId) throws Exception {
        String commandName = "getPost";
        log.info(commandName + " Controller", postId);
        return rabbitMQProducer.publishSynchronous(postId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
    }

    @GetMapping("{postId}/comments")
    public Object getPostComments(@PathVariable String postId) throws Exception {
        String commandName = "getPostComments";
        log.info(commandName + " Controller::Post Id={}", postId);
        return rabbitMQProducer.publishSynchronous(postId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
    }

    @PostMapping("upvote/{id}")
    public void upvotePost(@RequestParam String userNameId, @PathVariable String id) {
        String commandName = "upvotePost";
        log.info(commandName + " Controller::Post Id={}, User Id={}", id, userNameId);
        VotePostForm voteCommentForm = VotePostForm.builder()
                .postId(id)
                .userNameId(userNameId)
                .build();
        MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                voteCommentForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
    }

    @PostMapping("downvote/{id}")
    public void downvotePost(@RequestParam String userNameId, @PathVariable String id) {
        String commandName = "downvotePost";
        log.info(commandName + " Controller::Post Id={}, User Id={}", id, userNameId);
        VotePostForm voteCommentForm = VotePostForm.builder()
                .postId(id)
                .userNameId(userNameId)
                .build();
        MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                voteCommentForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
    }

    @GetMapping("/popularPosts")
    private Object getPopularPosts() throws Exception {
        String commandName = "getPopularPosts";
        log.info(commandName + " Controller");
        return rabbitMQProducer.publishSynchronous("",
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
    }
}