package com.ScalableTeam.reddit.app.followReddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.requestForms.FollowRedditForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;


@RestController
@Slf4j
public class FollowRedditController {
    @Autowired
    private FollowRedditService followRedditService;
    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private Config config;

    @RequestMapping(method = RequestMethod.POST, value = "/followReddit")
    private void followReddit(@RequestBody FollowRedditForm followRedditForm){
        log.info(generalConfig.getCommands().get("followReddit") + "Controller", followRedditForm);
        String commandName = "followReddit";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));

        rabbitMQProducer.publishAsynchronous(
                followRedditForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
//        return followRedditService.execute(followRedditForm);
    }
}