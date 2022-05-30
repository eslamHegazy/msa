package com.ScalableTeam.httpServer.controllers.reddit;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.reddit.BanUserForm;
import com.ScalableTeam.models.reddit.ViewReportsForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ModerationController {

    @Autowired
    private Config config;
    @Autowired
    private CommandsMapper commandsMapper;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.GET, value = "/viewReports/{modId}")
    public Object viewReports(@PathVariable String modId, @RequestParam String redditId) throws Exception {
        String command = "viewReports";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        ViewReportsForm viewReportsForm = new ViewReportsForm();
        viewReportsForm.setModId(modId);
        viewReportsForm.setRedditId(redditId);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, viewReportsForm);
        return rabbitMQProducer.publishSynchronous(
                queueName,
                commandName,
                viewReportsForm
        );
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/banUser")
    public void banUser(@RequestBody BanUserForm banUserForm) throws Exception {
        String command = "banUser";
        String commandName = commandsMapper.getReddit().get(command);
        String queueName = config.getQueues().getRequest().getReddit().get(command);
        log.info("Controller - Queue: {}, Command: {}, Payload: {}", queueName, commandName, banUserForm);
        rabbitMQProducer.publishAsynchronousToQueue(
                queueName,
                commandName,
                banUserForm,
                config.getQueues().getResponse().getReddit().get(command)
        );
    }
}
