package com.ScalableTeam.chat.app;

import com.ScalableTeam.chat.app.config.MessageConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MQReceiver {

    // Get command by reflection then call execute
    @RabbitListener(queues = MessageConfig.QUEUE_NAME)
    public String receiveMessage(Message message) throws ClassNotFoundException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msgJSON = new String(message.getBody());
            System.out.println("Message RECEIVED: " + mapper.readValue(msgJSON, Map.class));

            Map<String, Object> reqBody = mapper.readValue(msgJSON, Map.class);
            String commandName = (String) reqBody.get("Command");
            String chatType = (String) reqBody.get("ChatType");
            System.out.println(commandName + "Command");
            Class commandClass = Class.forName("com.ScalableTeam.chat.app." + chatType + "." + commandName + "Command");
            MyCommand command = (MyCommand) commandClass.getConstructor().newInstance();
            Map<String, Object> data = (Map<String, Object>) reqBody.get("Body");

            return command.execute(data).toString();
        } catch (Exception e) {
            System.out.println(e);
            return "Internal Server Error";
        }
    }
}
