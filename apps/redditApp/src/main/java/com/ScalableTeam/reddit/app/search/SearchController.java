package com.ScalableTeam.reddit.app.search;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.MessagePublisher;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/search")
public class SearchController extends MessagePublisher {
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @GetMapping("by_title/{title}")
    public String searchByTitle(@PathVariable String title) {
        final String COMMAND_NAME = "searchByTitle";

        String indicator = generalConfig.getCommands().get(COMMAND_NAME);
        log.info(indicator + "Controller::Post title={}", title);

        return (String) rabbitMQProducer.publishSynchronous(
                title,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(COMMAND_NAME));
    }

    @GetMapping("by_channel/{id}")
    public String searchByChannel(@PathVariable String id) {
        final String COMMAND_NAME = "searchByChannel";

        String indicator = generalConfig.getCommands().get(COMMAND_NAME);
        log.info(indicator + "Controller::Channel Id={}", id);

        return (String) rabbitMQProducer.publishSynchronous(id,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(COMMAND_NAME));
    }
}
