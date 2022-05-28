package com.ScalableTeam.reddit.app.post;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.models.reddit.VotePostForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;

@RestController
@Slf4j
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private CreatePostService createPostService;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private GetPostService getPostService;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private GetPopularPostsService getPopularPostsService;

    @PostMapping
    public void createPost(@RequestBody Post post) throws Exception {
        String indicator = generalConfig.getCommands().get("createPost");
        log.info(indicator + "Controller", post);
        String commandName = "createPost";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                post,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get("createPost"),
                messagePostProcessor);
        //return createPostService.execute(post);
    }

    @GetMapping("{postId}")
    private Object getPost(@PathVariable String postId) throws Exception {
        log.info(generalConfig.getCommands().get("getPost") + "Controller", postId);
        String commandName = "getPost";
        return (String) rabbitMQProducer.publishSynchronous(postId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
        //return getPostService.execute(postId);
    }

    @GetMapping("{postId}/comments")
    public Object getPostComments(@PathVariable String postId) throws Exception {
        String commandName = "getPostComments";
        String indicator = generalConfig.getCommands().get(commandName);
        log.info(indicator + "Controller::Post Id={}", postId);
        return rabbitMQProducer.publishSynchronous(postId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
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

    @GetMapping("/popularPosts")
    private Object getPopularPosts() throws Exception {
        log.info(generalConfig.getCommands().get("getPopularPost") + "Controller");
        String commandName = "getPopularPosts";
        return (String) rabbitMQProducer.publishSynchronous("",
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
        // return getPopularPostsService.execute(null);
    }
}