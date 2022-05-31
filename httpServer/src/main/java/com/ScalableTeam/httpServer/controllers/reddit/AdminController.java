package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.reddit.AssignModeratorsForm;
import com.ScalableTeam.models.reddit.CreateChannelForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdminController {
    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/channels")
    public void createChannel(@RequestBody CreateChannelForm createChannelForm) throws Exception {
        String command = "createChannel";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, createChannelForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                createChannelForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/channels")
    public void assignModerators(@RequestBody AssignModeratorsForm assignModeratorsForm) throws Exception {
        String command = "assignModerators";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, assignModeratorsForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                assignModeratorsForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }
}
