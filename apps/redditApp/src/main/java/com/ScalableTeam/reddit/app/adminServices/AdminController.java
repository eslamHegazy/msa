package com.ScalableTeam.reddit.app.adminServices;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
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
public class AdminController {
    @Autowired
    private CreateChannelService createChannelService;
    @Autowired
    private AssignModeratorsService assignModeratorsService;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @RequestMapping(method = RequestMethod.POST,value = "/channels")
    private void createChannel(@RequestBody CreateChannelForm createChannelForm) throws Exception {
        log.info(generalConfig.getCommands().get("createChannel") + "Controller", createChannelForm);
        String commandName="createChannel";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));

        rabbitMQProducer.publishAsynchronous(
                createChannelForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
        //return createChannelService.execute(createChannelForm);
    }
    @RequestMapping(method = RequestMethod.PUT,value="/channels")
    private void assignModerators(@RequestBody AssignModeratorsForm assignModeratorsForm) throws Exception {
        log.info(generalConfig.getCommands().get("assignModerators") + "Controller", assignModeratorsForm);
        String commandName="assignModerators";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));

        rabbitMQProducer.publishAsynchronous(
                assignModeratorsForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
        //return assignModeratorsService.execute(assignModeratorsForm);
    }


}
