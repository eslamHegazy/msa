package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.reddit.BookmarkChannelForm;
import com.ScalableTeam.models.reddit.BookmarkPostForm;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class BookmarkController {
    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/bookmarkPost")
    public void bookmarkPost(@RequestBody BookmarkPostForm bookmarkPostForm) throws Exception {
        String command = "bookmarkPost";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, bookmarkPostForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                bookmarkPostForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @RequestMapping(method = RequestMethod.POST, value = "/bookmarkChannel")
    public void bookmarkChannel(@RequestBody BookmarkChannelForm bookmarkChannelForm) throws Exception {
        String command = "bookmarkChannel";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, bookmarkChannelForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                bookmarkChannelForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }
}