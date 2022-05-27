package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.MessagePublisher;
import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.BanUserForm;
import com.ScalableTeam.reddit.app.requestForms.CreateChannelForm;
import com.ScalableTeam.reddit.app.requestForms.ViewReportsForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;

@RestController
@Slf4j
public class ModerationController extends MessagePublisher {

    @Autowired
    private ViewReportsService viewReportsService;

    @Autowired
    private BanUserService banUserService;

    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @RequestMapping(method = RequestMethod.GET,value="/viewReports/{modId}")
    private Object viewReports(@PathVariable String modId, @RequestParam String redditId) throws Exception {
        ViewReportsForm viewReportsForm = new ViewReportsForm();
        viewReportsForm.setModId(modId);
        viewReportsForm.setRedditId(redditId);
        log.info(generalConfig.getCommands().get("viewReports ") + "Controller", viewReportsForm);
        String commandName = "viewReports";
        return rabbitMQProducer.publishSynchronous(viewReportsForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
//        return viewReportsService.execute(viewReportsForm);

    }
    @RequestMapping(method = RequestMethod.PUT,value = "/banUser")
    private void banUser(@RequestBody BanUserForm banUserForm) throws Exception {
        log.info(generalConfig.getCommands().get("banUser") + " Controller ", banUserForm);
        String commandName = "banUser";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));

        rabbitMQProducer.publishAsynchronous(
                banUserForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
//        return banUserService.execute(banUserForm);
    }


}
