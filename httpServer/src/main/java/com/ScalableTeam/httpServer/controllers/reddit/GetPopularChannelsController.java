package com.ScalableTeam.httpServer.controllers.reddit;


import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GetPopularChannelsController {

    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(value = "/getPopularChannels", method = RequestMethod.GET)
    public Object getPopularChannels() throws Exception {
        String command = "getPopularChannels";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, "");
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                ""
        );
    }
}

