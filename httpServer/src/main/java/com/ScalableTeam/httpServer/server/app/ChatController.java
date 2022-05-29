package com.ScalableTeam.httpServer.server.app;

import com.ScalableTeam.amqp.LegacyRabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RestController
@RequestMapping("/chat")
public class ChatController {

    public LegacyRabbitMQProducer rabbitMQProducer;

    @Autowired
    public ChatController(LegacyRabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @PostMapping("/privateChat")
    public Object createPrivateChat(Map<String, Object> reqBody) {
        System.out.println(reqBody);
        return 0;
    }
}