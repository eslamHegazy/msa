package com.ScalableTeam.reddit.app.popularChannels;



import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.LegacyRabbitMQProducer;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GetPopularChannelsController {

    @Autowired
    private GetPopularChannelsService getPopularChannelsService;

    @Autowired
    private GeneralConfig generalConfig;

    @Autowired
    private LegacyRabbitMQProducer rabbitMQProducer;

    @Autowired
    private Config config;

    @RequestMapping(value = "/getPopularChannels",method = RequestMethod.GET)
    private Object getPopularChannels() throws Exception {
        log.info(generalConfig.getCommands().get("getPopularChannels") + "Controller");
        String commandName = "getPopularChannels";
        return (String) rabbitMQProducer.publishSynchronous("",
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
        //return readWallService.execute(userNameId);
    }
}

