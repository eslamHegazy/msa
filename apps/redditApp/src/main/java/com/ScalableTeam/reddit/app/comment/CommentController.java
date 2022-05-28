package com.ScalableTeam.reddit.app.comment;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.models.reddit.VoteCommentForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;

@RestController
@Slf4j
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private Config config;

    @PostMapping
    private void comment(@RequestBody Comment comment) throws Exception {
        log.info(generalConfig.getCommands().get("comment") + "Controller", comment);
        String commandName = "comment";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                comment,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get("comment"),
                messagePostProcessor);
        //return commentService.execute(comment);
    }

    @PostMapping("upvote/{id}")
    public void upvoteComment(@RequestParam String userNameId, @PathVariable String id) {
        String commandName = "upvoteComment";
        String indicator = generalConfig.getCommands().get(commandName);
        log.info(indicator + "Controller::Comment Id={}, User Id={}", id, userNameId);
        VoteCommentForm voteCommentForm = VoteCommentForm.builder()
                .commentId(id)
                .userNameId(userNameId)
                .build();
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));
        rabbitMQProducer.publishAsynchronous(
                voteCommentForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get("upvoteComment"),
                messagePostProcessor);
    }

    @PostMapping("downvote/{id}")
    public void downvoteComment(@RequestParam String userNameId, @PathVariable String id) {
        String commandName = "downvoteComment";
        String indicator = generalConfig.getCommands().get(commandName);
        log.info(indicator + "Controller::Comment Id={}, User Id={}", id, userNameId);
        VoteCommentForm voteCommentForm = VoteCommentForm.builder()
                .commentId(id)
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
