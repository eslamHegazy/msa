package com.ScalableTeam.reddit.app.reportPost;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.models.reddit.ReportPostForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@Slf4j
public class ReportPostController extends  MessagePublisher{

    @Autowired
    private ReportPostService reportPostService;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private Config config;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @RequestMapping(method = RequestMethod.POST,value = "/reportPost")
    private void reportPost(@RequestBody ReportPostForm reportPostForm) throws Exception {
        log.info(generalConfig.getCommands().get("reportPost") + "Controller", reportPostForm);
        String commandName ="reportPost";
        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getReddit().get(commandName));

       rabbitMQProducer.publishAsynchronous(
                reportPostForm,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName),
                messagePostProcessor);
//        return reportPostService.execute(reportPostForm);
    }
}
