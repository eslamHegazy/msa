package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @GetMapping("by_title/{title}")
    public Object searchByTitle(@PathVariable String title) throws Exception {
        String command = "searchByTitle";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, title);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                title
        );
    }

    @GetMapping("by_channel/{id}")
    public Object searchByChannel(@PathVariable String id) {
        String command = "searchByChannel";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, id);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                id
        );
    }
}
