package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.arango.Comment;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.reddit.VoteCommentForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @PostMapping
    public void comment(@RequestBody Comment comment) throws Exception {
        String command = "comment";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, comment);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                comment,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @PostMapping("upvote/{id}")
    public void upvoteComment(@RequestParam String userNameId, @PathVariable String id) throws Exception {
        String command = "upvoteComment";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        VoteCommentForm voteCommentForm = VoteCommentForm.builder()
                .commentId(id)
                .userNameId(userNameId)
                .build();

        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, voteCommentForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                voteCommentForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @PostMapping("downvote/{id}")
    public void downvoteComment(@RequestParam String userNameId, @PathVariable String id) throws Exception {
        String command = "downvoteComment";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        VoteCommentForm voteCommentForm = VoteCommentForm.builder()
                .commentId(id)
                .userNameId(userNameId)
                .build();

        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, voteCommentForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                voteCommentForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }
}
