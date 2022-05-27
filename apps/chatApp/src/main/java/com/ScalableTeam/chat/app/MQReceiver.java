package com.ScalableTeam.chat.app;

import com.ScalableTeam.chat.app.config.MessageConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class MQReceiver {

    // Get command by reflection then call execute
    @RabbitListener(queues = MessageConfig.QUEUE_NAME)
    public void receiveMessage(Map<String, Object> reqBody) throws ClassNotFoundException {
        try {
            System.out.println("Message RECEIVED: " + reqBody);
            String commandName = (String) reqBody.get("Command");
            String chatType = (String) reqBody.get("ChatType");
            System.out.println(commandName + "Command");
            Class commandClass = Class.forName("com.ScalableTeam.chat.app." + chatType + "." + commandName + "Command");
            MyCommand command = (MyCommand) commandClass.getConstructor().newInstance();
            Map<String, Object> data = (Map<String, Object>) reqBody.get("Body");
            command.execute(data);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
