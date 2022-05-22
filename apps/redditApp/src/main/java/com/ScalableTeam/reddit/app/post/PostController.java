package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.MessagePublisher;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.app.requestForms.VoteCommentForm;
import com.ScalableTeam.reddit.app.requestForms.VotePostForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController extends MessagePublisher {
    @Autowired
    private CreatePostService createPostService;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private GetPostService getPostService;

    @PostMapping
    public Post createPost(@RequestBody Post post) throws Exception {
        String indicator = generalConfig.getCommands().get("createPost");
        log.info(indicator + "Controller", post);
        return createPostService.execute(post);
    }

    @RequestMapping("/posts/{postId}")
    private Post getPost(@PathVariable String postId) throws Exception {
        log.info(generalConfig.getCommands().get("getPost") + "Controller", postId);
        return getPostService.execute(postId);
    }

    @PostMapping("upvote/{id}")
    public void upvotePost(@RequestParam String userNameId, @PathVariable String id) {
        String commandName = "upvotePost";
        String indicator = generalConfig.getCommands().get(commandName);
        log.info(indicator + "Controller::Post Id={}, User Id={}", id, userNameId);
        VotePostForm voteCommentForm = VotePostForm.builder()
                .postId(id)
                .userNameId(userNameId)
                .build();
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
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
        String indicator = generalConfig.getCommands().get(commandName);
        log.info(indicator + "Controller::Post Id={}, User Id={}", id, userNameId);
        VotePostForm voteCommentForm = VotePostForm.builder()
                .postId(id)
                .userNameId(userNameId)
                .build();
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                voteCommentForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
    }
}
