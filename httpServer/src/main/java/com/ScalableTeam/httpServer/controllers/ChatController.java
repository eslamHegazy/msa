package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.MessagePublisher;
import com.ScalableTeam.amqp.RabbitMQProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
@Slf4j
public class ChatController {

    @Autowired
    private Config config;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/group-chat/{chatId}")
    public Object getGroupChat(@PathVariable String chatId, @RequestParam String lastMessageId) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("ChatType", "GroupChat");
        msg.put("Command", "GetMessageByGroupId");
        Map<String, Object> body = new HashMap<>();
        body.put("chatId", chatId);
        body.put("lastMessageId", lastMessageId);
        msg.put("Body", body);
        System.out.println(msg);
        MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                "responseChatMQ");
        return rabbitMQProducer.publishSynchronous(msg, "generic_exchange", "chat_routingKey");
    }

    @PostMapping("/group-chat")
    public String CreateGroupChat(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "GroupChat");
            msg.put("Command", "CreateGroup");
            msg.put("Body", body);
            System.out.println(msg);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @PutMapping("/group-chat")
    public String UpdateGroupChat(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "GroupChat");
            msg.put("Command", "UpdateGroup");
            msg.put("Body", body);
            System.out.println(msg);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @DeleteMapping("/group-chat/{chatId}")
    public String DeleteroupChat(@RequestBody Map<String, Object> body, @PathVariable String chatId) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "GroupChat");
            msg.put("Command", "DeleteGroup");
            body.put("groupChatId", chatId);
            msg.put("Body", body);
            System.out.println(msg);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @PostMapping("/group-chat/addMember")
    public String addMemberToGroup(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "GroupChat");
            msg.put("Command", "AddMember");
            msg.put("Body", body);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @PostMapping("/group-chat/removeMember")
    public String removeMemberToGroup(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "GroupChat");
            msg.put("Command", "RemoveMember");
            msg.put("Body", body);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @PostMapping("/group-chat/message")
    public String messageToGroup(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "GroupChat");
            msg.put("Command", "SendMessageToGroup");
            msg.put("Body", body);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }


    @PostMapping("/private-chat")
    public String CreatePrivateChat(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "PrivateChat");
            msg.put("Command", "CreateChat");
            msg.put("Body", body);
            System.out.println(msg);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @PostMapping("/private-chat/message")
    public String SendMessagePrivateChat(@RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> msg = new HashMap<>();
            msg.put("ChatType", "PrivateChat");
            msg.put("Command", "AddMessage");
            msg.put("Body", body);
            System.out.println(msg);
            MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                    "responseChatMQ");
            rabbitMQProducer.publishAsynchronous(msg, "generic_exchange", "chat_routingKey", messagePostProcessor);

            return "0";
        } catch (Exception ex) {
            System.out.println(ex);
            return "Internal Server Error";
        }
    }

    @GetMapping("/private-chat/{chatId}")
    public Object getPrivateChat(@PathVariable String chatId, @RequestParam String lastMessageId) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("ChatType", "PrivateChat");
        msg.put("Command", "GetChat");
        Map<String, Object> body = new HashMap<>();
        body.put("chatId", chatId);
        body.put("lastMessageId", lastMessageId);
        msg.put("Body", body);
        System.out.println(msg);
        MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                "responseChatMQ");
        return rabbitMQProducer.publishSynchronous(msg, "generic_exchange", "chat_routingKey");
    }

    @GetMapping("/{userId}")
    public Object getAllChat(@PathVariable String userId) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("ChatType", "PrivateChat");
        msg.put("Command", "GetAllChats");
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        msg.put("Body", body);
        System.out.println(msg);
        MessagePostProcessor messagePostProcessor = MessagePublisher.getMessageHeaders(
                "responseChatMQ");
        return rabbitMQProducer.publishSynchronous(msg, "generic_exchange", "chat_routingKey");
    }
}
