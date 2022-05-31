package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.reddit.FollowRedditForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class FollowRedditController {
    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/followReddit")
    public void followReddit(@RequestBody FollowRedditForm followRedditForm) throws Exception {
        String command = "followReddit";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, followRedditForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                followRedditForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @RequestMapping(method = RequestMethod.POST, value = "/unfollowReddit")
    public void unFollowReddit(@RequestBody FollowRedditForm followRedditForm) throws Exception {
        String command = "unfollowReddit";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, followRedditForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                followRedditForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }
}