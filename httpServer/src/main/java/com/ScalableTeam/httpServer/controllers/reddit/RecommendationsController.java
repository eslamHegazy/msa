package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RecommendationsController {

    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.GET, value = "/redditRecommendations/{userNameId}")
    public Object redditsRecommendations(@PathVariable String userNameId) throws Exception {
        String command = "redditsRecommendations";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, userNameId);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                userNameId
        );
    }

    @RequestMapping(method = RequestMethod.GET, value = "/recommendationsBasedOnFollowers/{userNameId}")
    public String recommendationsBasedOnFollowersService(@PathVariable String userNameId) throws Exception {
        String command = "recommendationsBasedOnFollowersService";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, userNameId);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                userNameId
        );
    }
}
