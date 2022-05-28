package com.ScalableTeam.server.app;

import com.ScalableTeam.amqp.RabbitMQProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@Slf4j
public class ChatController {
    private final RabbitMQProducer rabbitMQProducer;

//    @PostMapping
//    public void CreateGroupChat(@RequestBody Object post) throws Exception {
//        String indicator = generalConfig.getCommands().get("createPost");
//        log.info(indicator + "Controller", post);
//        String commandName = "createPost";
//        MessagePostProcessor messagePostProcessor = getMessageHeaders(
//                config.getQueues().getResponse().getReddit().get(commandName));
//        rabbitMQProducer.publishAsynchronous(
//                post,
//                config.getExchange(),
//                config.getQueues().getRequest().getReddit().get("createPost"),
//                messagePostProcessor);
//        //return createPostService.execute(post);
//    }
}
