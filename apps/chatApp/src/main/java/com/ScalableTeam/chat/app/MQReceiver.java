package com.ScalableTeam.chat.app;

import com.ScalableTeam.chat.app.config.MessageConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class MQReceiver {

    private final Map<String, MyCommand> commandMap;

    // Get command by reflection then call execute
    @RabbitListener(queues = MessageConfig.QUEUE_NAME_SYNC)
    public Object receiveMessageAndReturn(Message message) throws ClassNotFoundException {
        try {

            ObjectMapper mapper = new ObjectMapper();
            String msgJSON = new String(message.getBody());
            System.out.println("Message RECEIVED SYNC: " + mapper.readValue(msgJSON, Map.class));

            Map<String, Object> reqBody = mapper.readValue(msgJSON, Map.class);
            String commandName = reqBody.get("Command") + "Command";
            MyCommand command = commandMap.get(commandName);
            System.out.println(commandName);
            Map<String, Object> data = (Map<String, Object>) reqBody.get("Body");

            Object out = command.execute(data);
            return out.toString();
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    @RabbitListener(queues = MessageConfig.QUEUE_NAME)
    public void receiveMessage(Message message) throws ClassNotFoundException {
        try {
            System.out.println(commandMap);
            System.out.println(message);
            ObjectMapper mapper = new ObjectMapper();
            String msgJSON = new String(message.getBody());
            System.out.println("Message RECEIVED: " + mapper.readValue(msgJSON, Map.class));

            Map<String, Object> reqBody = mapper.readValue(msgJSON, Map.class);
            String commandName = reqBody.get("Command") + "Command";
            MyCommand command = commandMap.get(commandName);
            System.out.println(command);
            Map<String, Object> data = (Map<String, Object>) reqBody.get("Body");

            command.execute(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
